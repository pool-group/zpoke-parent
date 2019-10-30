package com.zren.platform.zpoke.core.service.thread;

import com.alibaba.fastjson.JSON;
import com.assembly.common.redis.redisson.DistributedLock;
import com.assembly.common.redis.redisson.HLock;
import com.assembly.common.util.BeanUtil;
import com.assembly.common.util.DataUtil;
import com.assembly.common.util.LogUtil;
import com.google.common.collect.Lists;
import com.zren.platform.common.service.facade.dto.in.robotInfo.RobotInitInputModelDTO;
import com.zren.platform.core.model.domain.Model;
import com.zren.platform.zpoke.common.service.integration.api.robot.RobotPullServiceIntegration;
import com.zren.platform.zpoke.common.service.integration.entity.RobotInfoEntity;
import com.zren.platform.zpoke.common.util.enums.PlayActionEnum;
import com.zren.platform.zpoke.common.util.enums.TableLimitEnum;
import com.zren.platform.zpoke.common.util.enums.TableStatsEnum;
import com.zren.platform.zpoke.common.util.enums.UserTypeEnum;
import com.zren.platform.zpoke.common.util.tool.RedisCommon;
import com.zren.platform.core.model.domain.client.MatchClientModel;
import com.zren.platform.core.model.domain.machine.PlayerMachineModel;
import com.zren.platform.core.model.domain.machine.TableMachineModel;
import com.zren.platform.core.model.domain.server.*;
import com.zren.platform.core.model.enums.EventServeCode;
import com.zren.platform.zpoke.core.service.event.MatchEventService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Match Thread
 *
 * @author k.y
 * @version Id: MatchThread.java, v 0.1 2019年10月02日 下午18:08 k.y Exp $
 */
@Component
public class MatchThread implements ThreadFactory {

    private AtomicInteger threadIndex = new AtomicInteger(0);

    private RobotPullServiceIntegration robotPullServiceIntegration;
    private DistributedLock distributedLock;

    public MatchThread(RobotPullServiceIntegration robotPullServiceIntegration, DistributedLock distributedLock) {
        this.robotPullServiceIntegration=robotPullServiceIntegration;
        this.distributedLock=distributedLock;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r, "Matching Thread-" + this.threadIndex.incrementAndGet());
        return thread;
    }

    public class MatchRunnable implements Runnable {

        private Long expire=2*1000L;
        private final MatchClientModel model;
        private MatchEventService father;

        public MatchRunnable(MatchClientModel model, MatchEventService matchEventService) {
            this.model=model;
            this.father=matchEventService;
        }

        @Override
        public void run() {
            LogUtil.info(" ========== Start Matching ... ");
            Long endTime=System.currentTimeMillis()+expire;
//        while (){
            Set<String> tSet=this.father.zSetQuery(RedisCommon.getTablePlayerCountKey(model.getBrand(),model.getGameId(),model.getRoomId()), TableLimitEnum.ALLOW_NUMBER.getNumber(),TableLimitEnum.MAX_NUMBER.getNumber()-1);

            //暂时处理为创建桌台方便前端调试
            tSet=null;
            if(null==tSet||tSet.size()==0){
                createTable();
            }else {
                List<String> tList= Lists.newArrayList(tSet);
                int index= DataUtil.randomNumber(0,tList.size(),1)[0];
                double score=this.father.zSetIncrementScore(RedisCommon.getTablePlayerCountKey(model.getBrand(),model.getGameId(),model.getRoomId()),tList.get(index),1);
                if(score>TableLimitEnum.MAX_NUMBER.getNumber()){
                    this.father.zSetIncrementScore(RedisCommon.getTablePlayerCountKey(model.getBrand(),model.getGameId(),model.getRoomId()),tList.get(index),-1);
                    LogUtil.info(MessageFormat.format(" score > TableLimitEnum.MAX_NUMBER, createTable starting.. score={0}",score));
                    createTable();
                }else if (score<TableLimitEnum.MIN_NUMBER.getNumber()){
                    LogUtil.info(MessageFormat.format(" score < TableLimitEnum.MIN_NUMBER, createTable starting.. score={0}",score));
                    createTable();
                } else {
                    String tableId= tList.get(index);
                    TableMachineModel oTableMachine=new TableMachineModel();
                    try(HLock lock=distributedLock.getLock(RedisCommon.getTablePlayerLockKey(model.getBrand(),model.getGameId(),model.getRoomId(),tableId))) {
                        Boolean isLocked=lock.tryLock(0, 10, TimeUnit.SECONDS);
                        if(isLocked){
                            TableMachineModel tableMachineModel=this.father.findTableMachineModel(model);
                            BeanUtil.copyPropertiesIgnoreNull(tableMachineModel,oTableMachine);

                            Model joinModel=null;
                            PlayerMachineModel playerMachineModel=null;
                            if(tableMachineModel.getTableStatsEnum().equals(TableStatsEnum.STARTING)){
                                playerMachineModel=addPlayer(tableMachineModel.getPlayerList(),PlayActionEnum.LOOK_ON);
                                JoiningServerModel joiningServerModel=new JoiningServerModel();
                                joiningServerModel.setLeaderSeat(tableMachineModel.getLeaderSeat());
                                joiningServerModel.setCurrentSeat(tableMachineModel.getCurrentSeat());
                                joiningServerModel.setCommonCard(tableMachineModel.getCommonCard());

                                //暂时写死
                                joiningServerModel.setLeftTime(10000);
                                joiningServerModel.setTotalChip(tableMachineModel.getPlayerList().stream().map(PlayerMachineModel::getTotalBet).reduce(BigDecimal.ZERO, BigDecimal::add));
                                joiningServerModel.setTableState(TableStatsEnum.STARTING);
                                joiningServerModel.setPlayerServerModels(JSON.parseArray(JSON.toJSONString(tableMachineModel.getPlayerList()), PlayerServerModel.class));
                                joinModel=joiningServerModel;
                            }else if(tableMachineModel.getTableStatsEnum().equals(TableStatsEnum.WAIT_START)){
                                playerMachineModel=addPlayer(tableMachineModel.getPlayerList(),PlayActionEnum.JOIN);
                                JoinServerModel joinServerModel=new JoinServerModel();
                                joinServerModel.setTableState(TableStatsEnum.WAIT_START);
                                joinServerModel.setPlayerServerModels(JSON.parseArray(JSON.toJSONString(tableMachineModel.getPlayerList()), PlayerServerModel.class));
                                joinModel=joinServerModel;
                            }

                            this.father.initSeat();

                            this.father.hashPut(RedisCommon.getTablePlayerKey(model.getBrand(),model.getGameId(),model.getRoomId()),tableId, JSON.toJSONString(tableMachineModel));
                            this.father.zSetPut(RedisCommon.getTablePlayerCountKey(model.getBrand(),model.getGameId(),model.getRoomId()),tableId, tableMachineModel.getPlayerList().size());

                            joinModel.setCode(EventServeCode.JOIN.getCode());
                            joinModel.setTableNo(tableId);
                            this.father.sendById(Lists.newArrayList(playerMachineModel.getUserId()),joinModel,0L);

                            JoinBroadServerModel joinBroadServerModel=new JoinBroadServerModel();
                            joinBroadServerModel.setCode(EventServeCode.JOIN_BROAD.getCode());
                            joinBroadServerModel.setTableNo(tableId);
                            PlayerServerModel playerServerModel=new PlayerServerModel();
                            BeanUtil.copyPropertiesIgnoreNull(playerMachineModel,playerServerModel);
                            joinBroadServerModel.setPlayerServerModel(playerServerModel);
                            this.father.send(this.father.findOtherPlayer(model.getUserId()),joinBroadServerModel,0L);
                        }
                    } catch (Exception e) {
                        this.father.hashPut(RedisCommon.getTablePlayerKey(model.getBrand(),model.getGameId(),model.getRoomId()),tableId, JSON.toJSONString(oTableMachine));
                        this.father.zSetPut(RedisCommon.getTablePlayerCountKey(model.getBrand(),model.getGameId(),model.getRoomId()),tableId, oTableMachine.getPlayerList().size());
                        MatchServerModel matchServerModel=new MatchServerModel();
                        matchServerModel.setCode(EventServeCode.MATCH_FAIL.getCode());
                        this.father.sendById(Lists.newArrayList(model.getUserId()),matchServerModel,0L);
                        LogUtil.error(e," join table is exception!");
                    }
                }
            }
//        }
        }

    public void createTable(){
        String tableId=DataUtil.createSequenceUid().toString();
        TableMachineModel tableMachine=new TableMachineModel();
        try {
            List<PlayerMachineModel> playerMachineModelList= Lists.newArrayList();
            RobotInitInputModelDTO dto=new RobotInitInputModelDTO();
            dto.setBrand(model.getBrand());
            dto.setGameId(model.getGameId());
            dto.setRoomId(model.getRoomId());

//            int conut= DataUtil.randomNumber(3,8,1)[0];

            dto.setCount(3);
            //暂时写写死，后续改为动态查询
            dto.setMinAmount(BigDecimal.valueOf(100));
            List<RobotInfoEntity> entityList=robotPullServiceIntegration.createRobot(dto);
            for(RobotInfoEntity rf:entityList){
                PlayerMachineModel p=new PlayerMachineModel();
                p.setUserId(rf.getUserId());
                p.setUserName(rf.getUserName());
                p.setBrand(rf.getBrand());
                p.setGameId(dto.getGameId());
                p.setRoomId(dto.getRoomId());
                p.setBatchId(rf.getBatchId());
                p.setBalance(rf.getBalance());
                p.setFeatures(rf.getFeatures());
                p.setHeadUrl(rf.getHeadUrl());
                p.setPlayActionEnum(PlayActionEnum.READY);
                p.setUserTypeEnum(UserTypeEnum.ROBOT);
                playerMachineModelList.add(p);
            }
            addPlayer(playerMachineModelList,PlayActionEnum.READY);
            tableMachine.setPlayerList(playerMachineModelList);
            tableMachine.setBrand(dto.getBrand());
            tableMachine.setGameId(dto.getGameId());
            tableMachine.setRoomId(dto.getRoomId());
            tableMachine.setTableId(tableId);
            tableMachine.setUsableSeatList(Lists.newLinkedList(Arrays.asList(DataUtil.randomNumber(1,10,9))));
            tableMachine.setTableStatsEnum(TableStatsEnum.STARTING);

            StartServerModel startServerModel=new StartServerModel();
            startServerModel.setTableNo(tableId);
            startServerModel.setCode(EventServeCode.START.getCode());

            this.father.refreshTableMachine(tableMachine);
            this.father.initMetadata(startServerModel);

            PushPlayerServerModel pushPlayerServerModel=new PushPlayerServerModel();
            pushPlayerServerModel.setCode(EventServeCode.PUSH_PLAYER.getCode());
            pushPlayerServerModel.setTableNo(tableId);
            pushPlayerServerModel.setOffset(tableMachine.getOffset().getAndIncrement());
            List<PlayerServerModel> playerServerModelList=JSON.parseArray(JSON.toJSONString(tableMachine.getPlayerList()),PlayerServerModel.class);
            pushPlayerServerModel.setPlayerServerModels(playerServerModelList);
            this.father.send(tableMachine.getPlayerList(),pushPlayerServerModel,0L);

            startServerModel.setOffset(tableMachine.getOffset().getAndIncrement());

            this.father.hashPut(RedisCommon.getTablePlayerKey(model.getBrand(),model.getGameId(),model.getRoomId()),tableId, JSON.toJSONString(tableMachine));
            this.father.zSetPut(RedisCommon.getTablePlayerCountKey(model.getBrand(),model.getGameId(),model.getRoomId()),tableId, tableMachine.getPlayerList().size());

            tableMachine.getPlayerList().stream().filter(s->s.getUserTypeEnum().equals(UserTypeEnum.PLAYER)).forEach(s->{
                startServerModel.setFirstCard(s.getCards());
                startServerModel.setFirstCardType(s.getCardFirstEnum());
                this.father.send(Lists.newArrayList(s),startServerModel,10L);
            });

            this.father.forward();

        } catch (Exception e) {
            this.father.hashDelete(RedisCommon.getTablePlayerKey(model.getBrand(),model.getGameId(),model.getRoomId()),tableId);
            this.father.zSetDelete(RedisCommon.getTablePlayerCountKey(model.getBrand(),model.getGameId(),model.getRoomId()),tableId);
            MatchServerModel matchServerModel=new MatchServerModel();
            matchServerModel.setCode(EventServeCode.MATCH_FAIL.getCode());
            PlayerMachineModel playerMachineModel=new PlayerMachineModel();
            playerMachineModel.setUserTypeEnum(UserTypeEnum.PLAYER);
            playerMachineModel.setUserId(model.getUserId());
            this.father.send(Lists.newArrayList(playerMachineModel),matchServerModel,0L);
            LogUtil.error(e," create table is exception!");
        }
    }

        public PlayerMachineModel addPlayer(List<PlayerMachineModel> playerMachineModelList,PlayActionEnum playActionEnum){
            PlayerMachineModel player=new PlayerMachineModel();
            player.setUserId(model.getUserId());
            player.setUserName(model.getUserName());
            player.setBrand(model.getBrand());
            player.setGameId(model.getGameId());
            player.setRoomId(model.getRoomId());
            //暂时放一放
            player.setBalance(BigDecimal.valueOf(10000));
            player.setHeadUrl(DataUtil.randomNumber(1,13,1)[0]);
            player.setUserTypeEnum(UserTypeEnum.PLAYER);
            player.setPlayActionEnum(playActionEnum);
            playerMachineModelList.add(player);
            return player;
        }
    }

}
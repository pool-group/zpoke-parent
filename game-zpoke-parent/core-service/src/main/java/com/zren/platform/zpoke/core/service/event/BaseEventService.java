package com.zren.platform.zpoke.core.service.event;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.assembly.common.exception.BizRuntimeException;
import com.assembly.common.util.DataUtil;
import com.assembly.common.util.LogUtil;
import com.zren.platform.common.service.facade.api.robot.AIRobotRuleManage;
import com.zren.platform.common.service.facade.dto.in.rule.RuleInputModelDTO;
import com.zren.platform.common.service.facade.result.RobotBaseResult;
import com.zren.platform.core.model.context.MessageContext;
import com.zren.platform.core.model.domain.Model;
import com.zren.platform.core.model.domain.client.OvertimeClientModel;
import com.zren.platform.core.model.enums.EventClientCode;
import com.zren.platform.core.model.enums.EventServeCode;
import com.zren.platform.core.model.enums.RoundEnum;
import com.zren.platform.core.model.standard.Dispatcher;
import com.zren.platform.zpoke.common.service.integration.api.robot.ZpokeRobotClientIntegration;
import com.zren.platform.zpoke.common.service.integration.cache.BaseCacheService;
import com.zren.platform.zpoke.common.util.enums.PlayActionEnum;
import com.zren.platform.zpoke.common.util.enums.TableLimitEnum;
import com.zren.platform.zpoke.common.util.enums.TableStatsEnum;
import com.zren.platform.zpoke.common.util.enums.UserTypeEnum;
import com.zren.platform.zpoke.common.util.exception.IgnoreRuntimeException;
import com.zren.platform.zpoke.common.util.tool.RedisCommon;
import com.zren.platform.core.model.context.CardContext;
import com.zren.platform.core.model.context.Context;
import com.zren.platform.core.model.domain.PlayerModel;
import com.zren.platform.core.model.domain.client.PlayerClientModel;
import com.zren.platform.core.model.domain.machine.PlayerMachineModel;
import com.zren.platform.core.model.domain.machine.TableMachineModel;
import com.zren.platform.core.model.domain.server.StartServerModel;
import com.zren.platform.core.model.enums.TacticsEnum;
import com.zren.platform.core.model.factory.ZpokeCardFactory;
import com.zren.platform.core.model.standard.Event;
import com.zren.platform.zpoke.core.service.mq.RocketProducer;
import com.zren.platform.zpoke.core.service.queue.HandleDelayQueue;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Base Event Service
 *
 * @author k.y
 * @version Id: BaseEventService.java, v 0.1 2019年10月01日 下午15:09 k.y Exp $
 */
public abstract class BaseEventService extends BaseCacheService implements Event {

    @Autowired
    private RocketProducer rocketProducer;
    @Autowired
    protected HandleDelayQueue handleDelayQueue;
    @Autowired
    private AIRobotRuleManage aiRobotRuleManage;
    @Autowired
    private Dispatcher dispatcher;
    @Autowired
    private ZpokeRobotClientIntegration zpokeRobotClientIntegration;

    protected TableMachineModel tableMachineModel;

    protected void initModel(PlayerClientModel playerModel, Context context){
        JSONObject nativeJson=context.getNativeJson();
        playerModel.setCode(context.getCode());
        playerModel.setUserId(nativeJson.getLong("userId"));
        playerModel.setRoomId(nativeJson.getInteger("roomId"));
        playerModel.setTableNo(nativeJson.getString("tableNo"));
        String data=(String)this.hashFind(RedisCommon.getPlayerInfoKey(), playerModel.getUserId().toString());
        JSONObject jsonData=JSONObject.parseObject(data);
        playerModel.setUserName(jsonData.getString("username"));
        playerModel.setBrand(jsonData.getString("brand"));
        if(playerModel.getCode()!=EventClientCode.MATCH.getCode()){
            this.tableMachineModel=this.findTableMachineModel(playerModel);
            playerModel.setSid(playerModel.getTableNo()+this.tableMachineModel.getOffset());
        }
    }

    public void refreshTableMachine(TableMachineModel tableMachine){
        this.tableMachineModel=tableMachine;
    }

    public void check(PlayerClientModel playerClientModel,PlayerModel playerModel,PlayActionEnum playActionEnum){
        Optional<PlayerMachineModel> optional=tableMachineModel.getPlayerList().stream().filter(s->s.getSeat().equals(tableMachineModel.getCurrentSeat())).findFirst();
        if(optional.isPresent()){
            if(!optional.get().getUserId().equals(playerClientModel.getUserId()))
                throw new BizRuntimeException(String.format("No current player's turn : userId=[ %s ]",playerClientModel.getUserId()));
            else {
                if(playerClientModel.getCallBet().compareTo(findAvailableBalance(optional.get()))==1){
                    throw new RuntimeException(String.format("QuestParam: callBet=[ %s ] is illegal, can not > available balance .. ",playerClientModel.getCallBet()));
                }else if((playActionEnum.equals(PlayActionEnum.GIVE_UP)&&ifGiveUpOver())||ifOver()){
                    playerModel.setCurrentSeat(-1);
                    this.send(tableMachineModel.getPlayerList(),playerModel,0L);
                    this.hashPut(RedisCommon.getTablePlayerKey(playerClientModel.getBrand(),playerClientModel.getGameId(),playerClientModel.getRoomId()),playerClientModel.getTableNo(), JSON.toJSONString(tableMachineModel));
                    playerClientModel.setCode(EventClientCode.SETTLEMENT.getCode());
                    sendByMethod(JSON.toJSONString(playerClientModel));
//                    handleDelayQueue.addHandleEvent(JSON.toJSONString(playerClientModel), 0L);
                    throw new IgnoreRuntimeException();
                }else if(ifNextRound()){
                    playerModel.setCurrentSeat(-1);
                    this.send(tableMachineModel.getPlayerList(),playerModel,0L);
                    this.hashPut(RedisCommon.getTablePlayerKey(playerClientModel.getBrand(),playerClientModel.getGameId(),playerClientModel.getRoomId()),playerClientModel.getTableNo(), JSON.toJSONString(tableMachineModel));
                    playerClientModel.setCode(EventClientCode.NEXT_ROUND.getCode());
                    sendByMethod(JSON.toJSONString(playerClientModel));
//                    handleDelayQueue.addHandleEvent(JSON.toJSONString(playerClientModel), 1);
                    throw new IgnoreRuntimeException();
                }
            }
        }
    }

    public void send(List<PlayerMachineModel> users, Model model, Long delayTime){
        List<Long> userIds=users.stream().filter(s->s.getUserTypeEnum().equals(UserTypeEnum.PLAYER)).mapToLong(PlayerMachineModel::getUserId).boxed().collect(Collectors.toList());
        if(null==userIds||userIds.size()==0)
            return;
        handleDelayQueue.addHandleMessage(userIds, JSON.toJSONString(model),delayTime);
    }

    public void sendById(List<Long> userIds, Model model, Long delayTime){
        if(null==userIds||userIds.size()==0)
            return;
        handleDelayQueue.addHandleMessage(userIds, JSON.toJSONString(model),delayTime);
    }

    private void sendByMethod(String json){
        dispatcher.excute(new MessageContext(JSON.parseObject(json),""));
    }

    public void forward(){
        Optional<PlayerMachineModel> optional=tableMachineModel.getPlayerList().stream().filter(s->s.getSeat().equals(tableMachineModel.getCurrentSeat())&&s.getUserTypeEnum().equals(UserTypeEnum.ROBOT)).findFirst();
        if(optional.isPresent()){
            zpokeRobotClientIntegration.invoke(tableMachineModel);
        }
    }

    public void initStrategy(CardContext cardContext){
        List<PlayerMachineModel> playerList=tableMachineModel.getPlayerList();
        RuleInputModelDTO in = new RuleInputModelDTO();
        in.setBrand(tableMachineModel.getBrand());
        in.setGameId(tableMachineModel.getGameId());
        in.setRoomId(tableMachineModel.getRoomId());
        in.setTableId(tableMachineModel.getTableId());
        in.setUserIdlist(playerList.stream().filter(s->s.getUserTypeEnum().equals(UserTypeEnum.ROBOT)).mapToLong(PlayerMachineModel::getUserId).boxed().collect(Collectors.toList()));
        in.setPlayerlist(playerList.stream().filter(s->s.getUserTypeEnum().equals(UserTypeEnum.PLAYER)).mapToLong(PlayerMachineModel::getUserId).boxed().collect(Collectors.toList()));
        RobotBaseResult robotBaseResult=aiRobotRuleManage.createRule(in);
        if(robotBaseResult.isSuccess()){
            Map<String, Object> out = (Map<String, Object>) robotBaseResult.getResultObj();
            int robotry=(int) out.get("robotry");
            Long target=(Long) out.get("target");
            List<LinkedHashMap<String, Object>> zjhStrategyInfoList = (List<LinkedHashMap<String, Object>>) out.get("strategylist");
            if(TacticsEnum.SEC_MAX.getCode()==robotry){
                Optional<PlayerMachineModel> optional = playerList.stream()
                        .filter(p->p.getUserId().equals(target))
                        .findFirst();
                if(optional.isPresent()){
                    int[] cards=cardContext.getCardList().remove(cardContext.getCardList().size()-2);
                    optional.get().setCards(cards);
                    optional.get().setCardFirstEnum(ZpokeCardFactory.findCardFirstEnum(cards));
                }
            }else if(TacticsEnum.LOSS.getCode()==robotry){
                Optional<PlayerMachineModel> optional = playerList.stream()
                        .filter(p->p.getUserId().equals(target))
                        .findFirst();
                if(optional.isPresent()){
                    int[] cards=cardContext.getCardList().remove(cardContext.getCardList().size()-1);
                    optional.get().setCards(cards);
                    optional.get().setCardFirstEnum(ZpokeCardFactory.findCardFirstEnum(cards));
                }
            }

            playerList.stream().filter(s->s.getUserTypeEnum().equals(UserTypeEnum.ROBOT)).forEach(v->{
                for (LinkedHashMap<String, Object> model : zjhStrategyInfoList) {
                    if (v.getUserId()==((Integer)model.get("userId")).longValue()) {
                        v.setRobotry((Integer) model.get("isWin"));
                    }
                }
                if(TacticsEnum.GAIN.getCode()==v.getRobotry()){
                    if (TacticsEnum.GAIN.getCode()==v.getRobotry()){
                        int[] cards=cardContext.getCardList().remove(cardContext.getCardList().size()-1);
                        v.setCards(cards);
                        v.setCardFirstEnum(ZpokeCardFactory.findCardFirstEnum(cards));
                    }
                }else if(TacticsEnum.LOSS.getCode()==v.getRobotry()
                        ||TacticsEnum.RANDOM.getCode()==v.getRobotry()
                        ||TacticsEnum.SEC_MAX.getCode()==v.getRobotry()){
                    int l = DataUtil.randomNumber(0, cardContext.getCardList().size(), 1)[0];
                    int[] cards=cardContext.getCardList().remove(l);
                    v.setCards(cards);
                    v.setCardFirstEnum(ZpokeCardFactory.findCardFirstEnum(cards));
                }
            });

            playerList.stream().filter(s->s.getUserTypeEnum().equals(UserTypeEnum.PLAYER)&&(null==s.getCards()||s.getCards().length==0)).forEach(v->{
                int l = DataUtil.randomNumber(0, cardContext.getCardList().size(), 1)[0];
                int[] cards=cardContext.getCardList().remove(l);
                v.setCards(cards);
                v.setCardFirstEnum(ZpokeCardFactory.findCardFirstEnum(cards));
            });
        }else {
            throw new BizRuntimeException(String.format(" aiRobotRuleManage.createRule result: isSuccess=false, exception=[ %s ]",robotBaseResult.getErrorContext().fetchCurrentError()));
        }
    }

    public void initGameData(Model model, CardContext cardContext){
        List<PlayerMachineModel> playerList=tableMachineModel.getPlayerList();
        List<Integer> nums=tableMachineModel.getUsedSeatList();
        StartServerModel startServerModel=(StartServerModel)model;
        if(null==tableMachineModel.getLeaderSeat()){
            ZpokeCardFactory.create(2,53,playerList.size(),cardContext);
            tableMachineModel.setCommonOneCard(cardContext.getCommonOneCard());
            tableMachineModel.setCommonTwoCard(cardContext.getCommonTwoCard());
            tableMachineModel.setCommonThreeCard(cardContext.getCommonThreeCard());

//            int index= DataUtil.randomNumber(0,nums.size(),1)[0];

            int index=nums.size()-3;

            startServerModel.setLeaderSeat(nums.get(index));
            startServerModel.setSmallSeat(nums.get((index+1)>=nums.size()?(index+1)%nums.size():(index+1)));
            startServerModel.setBigSeat(nums.get((index+2)>=nums.size()?(index+2)%nums.size():(index+2)));
            startServerModel.setCurrentSeat(nums.get((index+3)>=nums.size()?(index+3)%nums.size():(index+3)));

            //暂时写死
            startServerModel.setBigBet(BigDecimal.valueOf(2));
            startServerModel.setSmallBet(BigDecimal.valueOf(1));
        }else {
            int index=nums.indexOf(tableMachineModel.getLeaderSeat());
            startServerModel.setLeaderSeat(nums.get((index+1)>=nums.size()?(index+1)%nums.size():(index+1)));
            startServerModel.setSmallSeat(nums.get((index+2)>=nums.size()?(index+2)%nums.size():(index+2)));
            startServerModel.setBigSeat(nums.get((index+3)>=nums.size()?(index+3)%nums.size():(index+3)));
            startServerModel.setCurrentSeat(nums.get((index+4)>=nums.size()?(index+4)%nums.size():(index+4)));

            //暂时写死
            startServerModel.setBigBet(BigDecimal.valueOf(2));
            startServerModel.setSmallBet(BigDecimal.valueOf(1));
        }
        tableMachineModel.setLeaderSeat(startServerModel.getLeaderSeat());
        tableMachineModel.setSmallSeat(startServerModel.getSmallSeat());
        tableMachineModel.setBigSeat(startServerModel.getBigSeat());
        tableMachineModel.setCurrentSeat(startServerModel.getCurrentSeat());

        playerList.stream().forEach(v->{
            if(v.getSeat().equals(startServerModel.getBigSeat())){
                v.setRoundBet(startServerModel.getBigBet());
                v.setTotalBet(startServerModel.getBigBet());
                v.setPlayActionEnum(PlayActionEnum.ADD);
            }else if(v.getSeat().equals(startServerModel.getSmallSeat())){
                v.setRoundBet(startServerModel.getSmallBet());
                v.setTotalBet(startServerModel.getSmallBet());
                v.setPlayActionEnum(PlayActionEnum.ADD);
            }
        });
    }

    public TableMachineModel findTableMachineModel(PlayerClientModel model){
        String tableInfo=(String)this.hashFind(RedisCommon.getTablePlayerKey(model.getBrand(),model.getGameId(),model.getRoomId()),model.getTableNo());
        return JSONObject.parseObject(tableInfo,TableMachineModel.class);
    }

    public void initSeat(){
        List<PlayerMachineModel> playerList=tableMachineModel.getPlayerList();
        LinkedList seatList=tableMachineModel.getUsableSeatList();
        for(int i=0;i<playerList.size();i++){
            Integer seat=playerList.get(i).getSeat();
            if(null==seat){
                playerList.get(i).setSeat((Integer) seatList.pop());
            }
            tableMachineModel.getUsedSeatList().add(playerList.get(i).getSeat());
        }
        Collections.sort(tableMachineModel.getUsedSeatList());
    }

    public void nextRoundProcess(PlayerClientModel playerClientModel,PlayerModel playerModel){
        if(tableMachineModel.getRoundEnum().equals(RoundEnum.ZERO)){
            playerClientModel.setCode(EventClientCode.SETTLEMENT.getCode());
            resetOver();
            this.handleDelayQueue.addHandleEvent(JSON.toJSONString(playerClientModel),0L);
            throw new IgnoreRuntimeException();
        }else {
            switch (tableMachineModel.getRoundEnum()){
                case ONE:
                    playerModel.setCode(EventServeCode.TWO_CARD.getCode());
                    tableMachineModel.setCommonCard(tableMachineModel.getCommonOneCard());
                    playerModel.setCards(tableMachineModel.getCommonOneCard());
                    break;
                case TWO:
                    playerModel.setCode(EventServeCode.THR_CARD.getCode());
                    playerModel.setCards(tableMachineModel.getCommonTwoCard());
                    tableMachineModel.setCommonCard(DataUtil.mergeArray(tableMachineModel.getCommonCard(),tableMachineModel.getCommonTwoCard()));
                    break;
                case THREE:
                    playerModel.setCode(EventServeCode.FOUR_CARD.getCode());
                    playerModel.setCards(tableMachineModel.getCommonThreeCard());
                    tableMachineModel.setCommonCard(DataUtil.mergeArray(tableMachineModel.getCommonCard(),tableMachineModel.getCommonThreeCard()));
                    break;
            }
            playerModel.setTableNo(playerClientModel.getTableNo());
            playerModel.setOffset(tableMachineModel.getOffset().getAndIncrement());
            int index=tableMachineModel.getUsedSeatList().indexOf(tableMachineModel.getLeaderSeat());
            AtomicReference<Integer> referenceShouldSeat=new AtomicReference();
            while (findPlayerAction(index,referenceShouldSeat)){
                index++;
            }
            playerModel.setCurrentSeat(referenceShouldSeat.get());
            tableMachineModel.setCurrentSeat(referenceShouldSeat.get());
            resetNext();
        }
    }

    private void resetNext(){
        tableMachineModel.setRoundEnum(tableMachineModel.getRoundEnum().next());
        tableMachineModel.getPlayerList().stream().filter(s->s.getSeat().equals(tableMachineModel.getCurrentSeat())).findFirst().get().setPlayActionEnum(PlayActionEnum.ADD);
        tableMachineModel.getPlayerList().stream().forEach(v->{
            if(!v.getPlayActionEnum().equals(PlayActionEnum.GIVE_UP)){
                v.setPlayActionEnum(PlayActionEnum.READY);
            }
            v.setRoundBet(BigDecimal.ZERO);
        });
    }

    private void resetOver(){
        tableMachineModel.setTableStatsEnum(TableStatsEnum.SETTLEMENT);
    }

    public void analysisProcess(PlayerClientModel playerClientModel,PlayerModel playerModel,PlayActionEnum playActionEnum){
        LogUtil.info(String.format(" initModel: QuestParam=[ %s ]",JSON.toJSONString(playerClientModel)));
        this.pollFirstEffective();
        playerModel.setTableNo(playerClientModel.getTableNo());
        playerModel.setCallBet(playerClientModel.getCallBet());
        playerModel.setOffset(tableMachineModel.getOffset().getAndIncrement());

        AtomicReference<Integer> actionSeat=new AtomicReference();
        tableMachineModel.getPlayerList().stream().filter(s->s.getUserId().equals(playerClientModel.getUserId())).forEach(v->{
            actionSeat.set(v.getSeat());
            v.setPlayActionEnum(playActionEnum);
            if(playerClientModel.getCode()!=EventClientCode.GIVE_UP.getCode()){
                v.setRoundBet(v.getRoundBet().add(playerClientModel.getCallBet()));
                v.setTotalBet(v.getTotalBet().add(playerClientModel.getCallBet()));
            }
        });
        playerModel.setActionSeat(actionSeat.get());
        playerModel.setMaxRoundBet(findMaxRoundBet());

        this.check(playerClientModel,playerModel,playActionEnum);

        int index=tableMachineModel.getUsedSeatList().indexOf(actionSeat.get());
        AtomicReference<Integer> referenceShouldSeat=new AtomicReference();
        while (findPlayerAction(index,referenceShouldSeat)){
            index++;
        }
        tableMachineModel.setCurrentSeat(referenceShouldSeat.get());
        playerModel.setCurrentSeat(referenceShouldSeat.get());

        OvertimeClientModel overtimeClientModel=new OvertimeClientModel();
        overtimeClientModel.setCode(EventClientCode.OVER_TIME.getCode());
        overtimeClientModel.setRoomId(tableMachineModel.getRoomId());
        overtimeClientModel.setTableNo(tableMachineModel.getTableId());
        overtimeClientModel.setOffset(tableMachineModel.getOffset().intValue());
        tableMachineModel.getPlayerList().stream().filter(s->s.getSeat().equals(tableMachineModel.getCurrentSeat())).forEach(v->{
            v.setActionTime(System.currentTimeMillis());
            v.setPlayActionEnum(PlayActionEnum.THINKING);
            overtimeClientModel.setUserId(v.getUserId());
        });
        this.addEffective();
        handleDelayQueue.addHandleEvent(JSON.toJSONString(overtimeClientModel), TableLimitEnum.MAX_OPERATE_TIME.getNumber());
        this.hashPut(RedisCommon.getTablePlayerKey(playerClientModel.getBrand(),playerClientModel.getGameId(),playerClientModel.getRoomId()),playerClientModel.getTableNo(), JSON.toJSONString(tableMachineModel));
        this.send(tableMachineModel.getPlayerList(),playerModel,0L);
        this.forward();
    }

    protected void countdown(TableMachineModel tableMachine){
        PlayerClientModel kickOutModel=new PlayerClientModel();
        kickOutModel.setCode(EventClientCode.OVER_TIME.getCode());
        kickOutModel.setRoomId(tableMachine.getRoomId());
        kickOutModel.setTableNo(tableMachine.getTableId());
        tableMachine.getPlayerList().stream().filter(s->s.getSeat().equals(tableMachine.getCurrentSeat())).forEach(v->{
            kickOutModel.setUserId(v.getUserId());
        });
        handleDelayQueue.addHandleEvent(JSON.toJSONString(kickOutModel), TableLimitEnum.MAX_OPERATE_TIME.getNumber());
    }

    private void addEffective(){
        tableMachineModel.getPlayerList().stream().filter(s->s.getSeat().equals(tableMachineModel.getCurrentSeat())).findFirst().get().getEffective().addLast(tableMachineModel.getOffset().intValue());
    }

    private void pollFirstEffective(){
        tableMachineModel.getPlayerList().stream().filter(s->s.getSeat().equals(tableMachineModel.getCurrentSeat())).findFirst().get().getEffective().pollFirst();
    }

    /**
     *  所有玩家：[GIVE_UP  FOLLOW  PASS] + [ADD] 计数等于桌台玩家数量并且加注玩家数量为1
     *  所有人加注次数达到3次
     *
     * @return
     */
    private boolean ifNextRound(){
        AtomicInteger var1=new AtomicInteger(0);
        AtomicInteger var2=new AtomicInteger(0);
        for(PlayerMachineModel play:tableMachineModel.getPlayerList()){
            if(play.getPlayActionEnum().equals(PlayActionEnum.GIVE_UP)
                    ||play.getPlayActionEnum().equals(PlayActionEnum.FOLLOW)
                    ||play.getPlayActionEnum().equals(PlayActionEnum.PASS)){var1.getAndIncrement();}
            if(play.getPlayActionEnum().equals(PlayActionEnum.ADD)){var2.getAndIncrement();}


        }
        int playerSeat=tableMachineModel.getPlayerList().stream().filter(s->s.getSeat().equals(tableMachineModel.getCurrentSeat())).findFirst().get().getSeat();
        if(RoundEnum.ONE.equals(tableMachineModel.getRoundEnum())){
            if(var1.get()+var2.get()==tableMachineModel.getPlayerList().size()&&tableMachineModel.getBigSeat().equals(playerSeat))
                return true;

        }else {
            if(var1.get()+var2.get()==tableMachineModel.getPlayerList().size())
                return true;
        }
        return false;
    }

    private boolean ifGiveUpOver(){
        AtomicInteger inx=new AtomicInteger(0);
        for(PlayerMachineModel play:tableMachineModel.getPlayerList()){
            if(play.getPlayActionEnum().equals(PlayActionEnum.GIVE_UP)){inx.getAndIncrement();}
        }
        if(inx.get()==tableMachineModel.getPlayerList().size()-1)
            return true;
        return false;
    }

    private boolean ifOver(){
        AtomicInteger var1=new AtomicInteger(0);
        AtomicInteger var2=new AtomicInteger(0);
        for(PlayerMachineModel play:tableMachineModel.getPlayerList()){
            if(play.getPlayActionEnum().equals(PlayActionEnum.ALL_IN)){var1.getAndIncrement();}
            else if(play.getPlayActionEnum().equals(PlayActionEnum.GIVE_UP)
                    ||play.getPlayActionEnum().equals(PlayActionEnum.FOLLOW)
                    ||play.getPlayActionEnum().equals(PlayActionEnum.PASS)){var2.getAndIncrement();}
        }
        if(var1.get()>0&&(var1.get()+var2.get())==tableMachineModel.getPlayerList().size())
            return true;
        return false;
    }

    private boolean findPlayerAction(int index,AtomicReference<Integer> referenceShouldSeat){
        Integer shouldSeat=tableMachineModel.getUsedSeatList().get((index+1)>=tableMachineModel.getUsedSeatList().size()?(index+1)%tableMachineModel.getUsedSeatList().size():(index+1));
        referenceShouldSeat.set(shouldSeat);
        PlayActionEnum playActionEnum=tableMachineModel.getPlayerList().stream().filter(s->s.getSeat().equals(shouldSeat)).findFirst().get().getPlayActionEnum();
        if(playActionEnum.equals(PlayActionEnum.GIVE_UP))
            return true;
        return false;
    }

    public BigDecimal findMaxRoundBet(){
        Optional<PlayerMachineModel> optional=tableMachineModel.getPlayerList().stream().max((r1,r2)->r1.getRoundBet().compareTo(r2.getRoundBet()));
        if(optional.isPresent()){
            return optional.get().getRoundBet();
        }
        return null;
    }

    public BigDecimal findAvailableBalance(PlayerMachineModel play){
        return play.getBalance().subtract(play.getTotalBet());
    }

    public List<PlayerMachineModel> findOtherPlayer(Long userId){
        return tableMachineModel.getPlayerList().stream().filter(s->!s.getUserId().equals(userId)).collect(Collectors.toList());
    }
}
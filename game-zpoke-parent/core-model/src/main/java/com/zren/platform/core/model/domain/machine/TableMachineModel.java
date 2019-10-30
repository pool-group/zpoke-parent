package com.zren.platform.core.model.domain.machine;

import com.google.common.collect.Lists;
import com.zren.platform.zpoke.common.util.enums.TableStatsEnum;
import com.zren.platform.core.model.domain.Machine;
import com.zren.platform.core.model.enums.RoundEnum;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Table Model
 *
 * @author k.y
 * @version Id: TableModel.java, v 0.1 2019年10月02日 下午14:20 k.y Exp $
 */
@Data
public class TableMachineModel extends Machine {

    private String tableId;
    private TableStatsEnum tableStatsEnum=TableStatsEnum.WAIT_START;
    private List<PlayerMachineModel> playerList;
    private LinkedList<Integer> usableSeatList;
    private LinkedList<Integer> usedSeatList= Lists.newLinkedList();
    private Integer leaderSeat;
    private Integer smallSeat;
    private Integer bigSeat;
    private Integer currentSeat;
    private RoundEnum roundEnum=RoundEnum.ONE;
    private int[] commonCard;
    private int[] commonOneCard;
    private int[] commonTwoCard;
    private int[] commonThreeCard;
    private AtomicInteger offset=new AtomicInteger(1);
}
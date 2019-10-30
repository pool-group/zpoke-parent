package com.zren.platform.zpoke.core.service.event;

import com.assembly.common.redis.redisson.DistributedLock;
import com.zren.platform.core.model.context.CardContext;
import com.zren.platform.core.model.context.Context;
import com.zren.platform.core.model.domain.Model;
import com.zren.platform.core.model.domain.client.MatchClientModel;
import com.zren.platform.core.model.domain.machine.TableMachineModel;
import com.zren.platform.core.model.enums.EventClientCode;
import com.zren.platform.zpoke.common.service.integration.api.robot.RobotPullServiceIntegration;
import com.zren.platform.zpoke.core.service.thread.MatchThread;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Match Event Service Impl
 *
 * @author k.y
 * @version Id: MatchEventServiceImpl.java, v 0.1 2019年09月30日 下午17:12 k.y Exp $
 */
@RequiredArgsConstructor
@Service
public class MatchEventService extends BaseEventService {

    private final RobotPullServiceIntegration robotPullServiceIntegration;
    private final DistributedLock distributedLock;

    @Override
    public void invoke(Context context) {
        MatchClientModel model=new MatchClientModel();
        this.initModel(model,context);
        ExecutorService executorService=null;
        try {
            MatchThread matchThread=new MatchThread(robotPullServiceIntegration,distributedLock);
            executorService=Executors.newSingleThreadExecutor(matchThread);
            executorService.execute(matchThread.new MatchRunnable(model,this));
        } finally {
            executorService.shutdown();
        }
    }

    public void initMetadata(Model model){
        CardContext cardContext=new CardContext();
        this.initSeat();
        this.initGameData(model,cardContext);
        this.initStrategy(cardContext);
    }

    @Override
    public int getCode() {
        return EventClientCode.MATCH.getCode();
    }
}
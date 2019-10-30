package com.zren.platform.common.service.integration.api.balance;

import com.assembly.common.util.BeanUtil;
import com.assembly.common.util.LogUtil;
import com.jxinternet.platform.service.balance.game.entity.UserBalance;
import com.jxinternet.platform.service.balance.game.service.BalanceGamePlayService;
import com.zren.platform.common.service.integration.entity.UserBalanceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

/**
 * Balance Service Integration
 *
 * @author k.y
 * @version Id: BalanceServiceIntegration.java, v 0.1 2019年10月01日 下午16:11 k.y Exp $
 */
@Service
public class BalanceServiceIntegration {

//    @Autowired
//    private BalanceGamePlayService balanceGamePlayService;
//
//    public UserBalanceEntity queryUserBalance(Long userId){
//        UserBalanceEntity userBalanceEntity=new UserBalanceEntity();
//        UserBalance userBalance=balanceGamePlayService.queryUserBalance(userId);
//        LogUtil.info(MessageFormat.format("BalanceServiceIntegration.queryUserBalance: userBalance=[ {0} ]",userBalance));
//        BeanUtil.copyPropertiesIgnoreNull(userBalance,userBalanceEntity);
//        return userBalanceEntity;
//    }

}
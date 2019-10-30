package com.zren.platform.zpoke.core.service.event;

import com.alibaba.fastjson.JSONObject;
import com.zren.platform.zpoke.common.util.tool.RedisCommon;
import com.zren.platform.core.model.context.Context;
import com.zren.platform.core.model.domain.client.ConnectClientModel;
import com.zren.platform.core.model.enums.EventClientCode;
import org.springframework.stereotype.Service;

/**
 * Connect Event Service
 *
 * @author k.y
 * @version Id: ConnectEventService.java, v 0.1 2019年10月01日 下午11:54 k.y Exp $
 */
@Service
public class ConnectEventService extends BaseEventService {

    @Override
    public void invoke(Context context) {
        ConnectClientModel model=new ConnectClientModel();
        model.setData(context.getNativeJson().getString("data"));
        this.hashPut(RedisCommon.getPlayerInfoKey(),JSONObject.parseObject(model.getData()).getString("userId"),model.getData());
    }

    @Override
    public int getCode() {
        return EventClientCode.CONNECT.getCode();
    }
}
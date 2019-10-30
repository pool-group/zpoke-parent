package com.zren.platform.core.model.context;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * Message Context
 *
 * @author k.y
 * @version Id: MessageContext.java, v 0.1 2019年10月01日 下午09:08 k.y Exp $
 */
@Data
public class MessageContext extends Context {

    public MessageContext(JSONObject json, String tag) {
        super(json,json.getIntValue("code"),tag,json.getString("topic"));
    }
}
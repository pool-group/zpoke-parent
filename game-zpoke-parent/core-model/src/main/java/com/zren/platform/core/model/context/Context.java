package com.zren.platform.core.model.context;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * Context
 *
 * @author k.y
 * @version Id: Context.java, v 0.1 2019年10月01日 下午09:12 k.y Exp $
 */
@Data
public abstract class Context extends BaseSerializable {

    private JSONObject nativeJson;

    private int code;

    private String tag;

    private String topic;

    public Context(JSONObject nativeJson, int code, String tag, String topic) {
        this.nativeJson = nativeJson;
        this.code = code;
        this.tag = tag;
        this.topic = topic;
    }
}
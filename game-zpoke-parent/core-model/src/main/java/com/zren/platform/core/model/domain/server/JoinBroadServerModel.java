package com.zren.platform.core.model.domain.server;

import com.zren.platform.core.model.domain.Model;
import lombok.Data;


/**
 * Join Server Model
 *
 * @author k.y
 * @version Id: JoinServerModel.java, v 0.1 2019年10月03日 下午20:00 k.y Exp $
 */
@Data
public class JoinBroadServerModel extends Model {

    private PlayerServerModel playerServerModel;
}
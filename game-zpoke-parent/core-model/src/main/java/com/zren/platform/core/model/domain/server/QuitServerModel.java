package com.zren.platform.core.model.domain.server;

import com.zren.platform.core.model.domain.PlayerModel;
import lombok.Data;

/**
 * @author k.y
 * @version Id: QuitServerModel.java, v 0.1 2019年10月24日 下午16:16 k.y Exp $
 */
@Data
public class QuitServerModel extends PlayerModel {

    private int seat;
}
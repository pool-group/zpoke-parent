package com.zren.platform.core.model.domain.server;

import com.zren.platform.core.model.domain.Model;
import com.zren.platform.zpoke.common.util.enums.TableStatsEnum;
import lombok.Data;

import java.util.List;

/**
 * Join Server Model
 *
 * @author k.y
 * @version Id: JoinServerModel.java, v 0.1 2019年10月03日 下午20:00 k.y Exp $
 */
@Data
public class JoinServerModel extends Model {

    private List<PlayerServerModel> playerServerModels;
    private TableStatsEnum tableState;
}
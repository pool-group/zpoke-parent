package com.zren.platform.core.model.domain.server;

import com.zren.platform.core.model.domain.Model;
import lombok.Data;

import java.util.List;

/**
 * @author k.y
 * @version Id: PushPlayerServerModel.java, v 0.1 2019年10月19日 下午10:45 k.y Exp $
 */
@Data
public class PushPlayerServerModel extends Model {

    private List<PlayerServerModel> playerServerModels;
}
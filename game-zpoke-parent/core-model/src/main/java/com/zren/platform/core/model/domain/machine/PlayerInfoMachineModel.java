package com.zren.platform.core.model.domain.machine;

import com.zren.platform.core.model.context.BaseSerializable;
import lombok.Data;

/**
 * @author k.y
 * @version Id: PlayerInfoMachineModel.java, v 0.1 2019年10月23日 下午13:40 k.y Exp $
 */
@Data
public class PlayerInfoMachineModel extends BaseSerializable {

    private String agentId="888888";
    private String clientId="888888";
    private String ip="0.0.0.0";
    private Integer avatar=1;
    private Long userId;
    private String brand;
    private String device="default";
    private Integer roomId;
    private String username;
    private Long timestamp;
}

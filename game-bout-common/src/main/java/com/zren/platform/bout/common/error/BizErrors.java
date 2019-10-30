package com.zren.platform.bout.common.error;

/**
 * @description 系统异常方案
 * @author gavin.lyu
 * @since 2018-11-17 18:00
 */
public class BizErrors {
	public static final LogicError E2000 = new LogicError("2000", "参数错误");
	public static final LogicError E2001 = new LogicError("2001", "登录失败");
    public static final LogicError E2002 = new LogicError("2002", "非法定义");
    
    public static final LogicError E4000 = new LogicError("4000", "创建桌台失败");
    public static final LogicError E4001 = new LogicError("4001", "桌台空闲座位不足");
    public static final LogicError E4002 = new LogicError("4002", "移除桌台资源失败");

}

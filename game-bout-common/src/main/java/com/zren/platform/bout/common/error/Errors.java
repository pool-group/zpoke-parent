package com.zren.platform.bout.common.error;

 import java.sql.SQLException;
 import java.util.LinkedHashMap;
 import java.util.Map;

 public class Errors
 {
   public static final LogicError E000 = new LogicError("000", "未知异常");
   public static final LogicError E001 = new LogicError("001", "网络错误");
   public static final LogicError E002 = new LogicError("002", "应用层未知异常");
   public static final LogicError E003 = new LogicError("003", "数据访问未知异常");
   public static final LogicError E004 = new LogicError("004", "缓存访问未知异常");
   public static final LogicError E005 = new LogicError("005", "RPC调用未知异常");
   public static final LogicError E006 = new LogicError("006", "鉴权异常(authentication error)");
   public static final LogicError E007 = new LogicError("007", "授权异常(authorization error)");
   
 
   public static final LogicError E100 = new LogicError("100", "传入的参数不对");
   public static final LogicError E101 = new LogicError("101", "JSON字符串语法不正确");
   public static final LogicError E102 = new LogicError("102", "请求的JSON串为空");
   public static final LogicError E106 = new LogicError("106", "解析json-xml流为JSON对象出错");
   public static final LogicError E108 = new LogicError("108", "content-type MIME unsupported");
   public static final LogicError E109 = new LogicError("109", "XML解析成MAP对象出错");
   public static final LogicError E110 = new LogicError("110", "HTTPerror, action not found!");
   public static final LogicError E111 = new LogicError("111", "鉴权不通过(access authorization failure)");
   public static final LogicError E112 = new LogicError("112", "HTTP 格式化输出异常(format exception)");
   public static final LogicError E113 = new LogicError("113", "HTTP 执行Preprocessor失败(call preprocessor exception)");
   public static final LogicError E114 = new LogicError("114", "操作超时(operation timeout)");
   public static final LogicError E115 = new LogicError("115", "数据加解密失败(encrypte failure)");
   public static final LogicError E116 = new LogicError("116", "数据解压缩失败(compress failure)");
   public static final LogicError E117 = new LogicError("117", "网络异常,请检查网络连接是否正常(HG)");
   public static final LogicError E118 = new LogicError("118", "网络异常,请检查网络连接是否正常(HP)");
   public static final LogicError E119 = new LogicError("119", "FTP 获取服务器上的文件列表异常");
   public static final LogicError E120 = new LogicError("120", "FTP 初始化连接服务器异常");
   public static final LogicError E121 = new LogicError("121", "FTP 关闭连接异常");
   public static final LogicError E122 = new LogicError("122", "FTP 获取服务器上的文件列表异常");
   public static final LogicError E123 = new LogicError("123", "FTP 初始化连接服务器异常");
   public static final LogicError E124 = new LogicError("124", "FTP 关闭连接异常");
   public static final LogicError E125 = new LogicError("125", "HTTP 请求处理异常");
   
 
   public static final LogicError E301 = new LogicError("301", "数据库执行超时(sql execute timeout)");
   public static final LogicError E302 = new LogicError("302", "数据库执行DML(update) SQL语句异常");
   public static final LogicError E303 = new LogicError("303", "数据库执行QUERY(select) SQL语句异常");
   public static final LogicError E304 = new LogicError("304", "没有找到数据库(db not found)");
   public static final LogicError E305 = new LogicError("305", "数据库执行事务控制的SQL语句异常(sql transaction fail)");
   public static final LogicError E306 = new LogicError("306", "数据库执行SQL语句, 影响行数(affacted)异常");
   public static final LogicError E307 = new LogicError("307", "解析动态SQL异常");
   public static final LogicError E330 = new LogicError("330", "目标数据源不可用(db connection error)");
   public static final LogicError E331 = new LogicError("331", "匹配数据库节点集群失败");
   public static final LogicError E332 = new LogicError("332", "SQL语法错误(sql syntax error)");
   public static final LogicError E333 = new LogicError("333", "数据库操作失败(db operation error)");
   public static final LogicError E334 = new LogicError("334", "表记录主键冲突(duplicate table row)");
   public static final LogicError E335 = new LogicError("335", "数据库授权不足(db Invalid Authorization)");
   public static final LogicError E336 = new LogicError("336", "数据库数据异常(db data exception)");
   public static final LogicError E337 = new LogicError("337", "数据库数据链路异常(communication exception)");
   
 
   public static final LogicError E401 = new LogicError("401", "进程内缓存操作异常");
   public static final LogicError E402 = new LogicError("402", "分布式缓存操作异常");
   public static final LogicError E403 = new LogicError("403", "初始化缓存异常");
   public static final LogicError E404 = new LogicError("404", "分布式锁(缓存实现)异常");
   
 
   public static final LogicError E501 = new LogicError("501", "Server Interval Error! ");
   public static final LogicError E502 = new LogicError("502", "Method not found! ");
   public static final LogicError E503 = new LogicError("503", "json-rpc protocal Error! ");
   public static final LogicError E504 = new LogicError("504", "mvc-action duplicate definition! ");
   
 
   public static final LogicError E700 = new LogicError("700", "缺少参数");
   public static final LogicError E701 = new LogicError("701", "required缺少参数");
   public static final LogicError E702 = new LogicError("702", "integer: 没有或者必须为整数 无参数");
   public static final LogicError E703 = new LogicError("703", "integerRange:整数范围必须在参数0和参数1之间; param0和param1必须能被转化成整数");
   public static final LogicError E704 = new LogicError("704", "date: 必须为日期格式, param0必须为yyyy-mm-dd");
   public static final LogicError E705 = new LogicError("705", "enum:必须是一系列枚举值中的一个（param0中用逗号分割出来的集合）");
   public static final LogicError E706 = new LogicError("706", "minLength: 参数最小长度不正确");
   public static final LogicError E707 = new LogicError("707", "maxLength: 参数最大长度不正确");
   public static final LogicError E708 = new LogicError("708", "mask: 允许自定义正则表达式来进行校验, param0为表达式字符串");
   public static final LogicError E709 = new LogicError("709", "double: 允许为空或者必须为double数 无参数");
   public static final LogicError E710 = new LogicError("710", "doubleRange:浮点范围必须在参数0和参数1之间; param0和param1必须能被转化成浮点数");
   public static final LogicError E711 = new LogicError("711", "参数类型不匹配");
   public static final LogicError E712 = new LogicError("712", "变量值不相等");
   public static final LogicError E713 = new LogicError("713", "格式化数据异常");
   public static final LogicError E714 = new LogicError("714", "java bean赋值异常");
   public static final LogicError E715 = new LogicError("715", "访问java bean属性异常");
   public static final LogicError E716 = new LogicError("716", "调用java bean方法异常");
   public static final LogicError E717 = new LogicError("717", "对象序列化异常");
   public static final LogicError E718 = new LogicError("718", "对象反序列化异常");
   public static final LogicError E719 = new LogicError("719", "未找到目标对象");
   public static final LogicError E720 = new LogicError("720", "编码解码数据异常");
   public static final LogicError E721 = new LogicError("721", "加解密数据异常");
   public static final LogicError E722 = new LogicError("722", "java 没有默认构造方法,无法创建实例");
   public static final LogicError E723 = new LogicError("723", "file operation error.");
   public static final LogicError E724 = new LogicError("724", "file not exists.");
   public static final LogicError E725 = new LogicError("725", "创建java类实例异常(new instance error)");
   public static final LogicError E726 = new LogicError("726", "参数数据长度不够(param length is too small)");
   public static final LogicError E727 = new LogicError("727", "参数值无效(param is invalid)");
   public static final LogicError E728 = new LogicError("728", "数据状态异常(data error)");
   
 
 
 
   public Errors() {}
   
 
 
 
   public static void throwEx(LogicError type, Throwable cause)
   {
     throw clone(type, cause);
   }
   
 
 
 
 
   public static void throwEx(LogicError type)
   {
     throw clone(type);
   }
   
 
 
 
 
 
 
   public static LogicError valueOf(Throwable ex)
   {
     LogicError e = null;
     if ((ex instanceof LogicError)) {
       e = (LogicError)ex;
     } else if ((ex instanceof NullPointerException)) {
       e = clone(E700, ex);
     } else if ((ex instanceof ClassCastException)) {
       e = clone(E711, ex);
     } else if ((ex instanceof SQLException))
     {
       SQLException se = (SQLException)ex;
       String name = se.getClass().getSimpleName().toLowerCase();
       if (name.startsWith("mysql")) {
         if (name.contains("integrityconstraintviolation")) {
           e = clone(E334, ex);
         } else if (name.contains("invalidauthorization")) {
           e = clone(E335, ex);
         } else if (name.contains("data")) {
           e = clone(E336, ex);
         } else if (name.contains("syntaxerror")) {
           e = clone(E332, ex);
         } else if (name.contains("timeout")) {
           e = clone(E301, ex);
         } else if (name.contains("connection")) {
           e = clone(E330, ex);
         } else {
           e = clone(E003, ex);
         }
       } else if (name.contains("communications")) {
         e = clone(E337, ex);
       } else {
         e = clone(E333, ex);
       }
     } else {
       e = clone(E000, ex);
     }
     return e;
   }
   
 
 
 
 
 
 
 
   public static LogicError valueOf(Throwable ex, LogicError wrapType)
   {
     LogicError are = null;
     if ((ex instanceof LogicError)) {
       are = (LogicError)ex;
     } else {
       are = clone(wrapType, ex);
     }
     return are;
   }
   
   public static LogicError valueOf(LogicError wrapType, Throwable ex) {
     return valueOf(ex, wrapType);
   }
   
 
 
 
 
 
 
   public static LogicError clone(LogicError type, Throwable cause)
   {
     LogicError e = new LogicError();
     e.setCode(type.getCode());
     e.setMsg(type.getMsg());
     e.setReason(type.getReason());
     e.setCause(cause);
     e.setAction(type.getAction());
     return e;
   }
   
 
 
 
 
 
 
   public static LogicError clone(LogicError type, String reason)
   {
     LogicError e = clone(type);
     e.setReason(reason);
     return e;
   }
   
 
 
 
 
 
   public static LogicError clone(LogicError type)
   {
     LogicError e = new LogicError();
     e.setCode(type.getCode());
     e.setMsg(type.getMsg());
     e.setAction(type.getAction());
     return e;
   }
   
 
 
 
 
 
   public static Map<String, String> toMap(LogicError ex)
   {
     if (null == ex) {
       return null;
     }
     
     Map<String, String> errorMap = new LinkedHashMap<String,String>();
     errorMap.put("code", ex.getCode());
     errorMap.put("message", ex.getMsg());
     errorMap.put("reason", ex.getReason());
     errorMap.put("action", ex.getAction());
     return errorMap;
   }
   
 
 
 
 
 
   public static LogicError parse(Map<String, String> errorMap)
   {
     if (null == errorMap) {
       return null;
     }
     
     LogicError ex = new LogicError();
     ex.setCode((String)errorMap.get("code"));
     ex.setMsg((String)errorMap.get("message"));
     ex.setReason((String)errorMap.get("reason"));
     ex.setAction((String)errorMap.get("action"));
     return ex;
   }
 }
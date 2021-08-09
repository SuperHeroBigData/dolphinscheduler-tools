package iquantex.com.dolphinscheduler.api;


import iquantex.com.dolphinscheduler.pojo.Login;
import iquantex.com.dolphinscheduler.pojo.Result;

/**
 * @author mujp
 */
public interface LoggerModel {
    /**
     * 查看task日志
     * @param login 登录信息
     * @param taskName 任务名
     * @return 执行状态
     */
   Result logClientService(Login login, String taskName);
}

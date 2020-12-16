package iquantex.com.dolphinscheduler.command;


import iquantex.com.dolphinscheduler.pojo.*;
import iquantex.com.entity.SheetEnv;

import java.util.List;

/**
 * @author mujp
 */
public interface WorkFlowModel {
    /**
     * 创建工作流
     * @param login
     * @param processDefinitionJson
     * @return
     */
    Result createWorkFlow(SheetEnv login, ProcessDefinition processDefinitionJson);

    /**
     * 提交工作流
     * @param loginUser
     * @param loginPassword
     * @param hostName
     * @param receivers 预警邮件多个用 “，” 隔开
     * @param jobName
     * @param projectName
     * @return
     */
    Result startJob(String loginUser, String loginPassword, String hostName, String receivers,String jobName, String projectName);

    /**
     * 作业部署状态
     * @param lineState
     * @param result
     * @param hostName
     */
    void releaseState(LineState lineState, Result result, String hostName);

    /**
     * 更新任务
     * @param local 本地参数
     * @param jobName 任务名
     * @param projectName 项目名
     * @param loginUser 登录用户
     * @param loginPassword 登录密码
     * @param hostName 访问域名
     * @param result 返回对象
     */
    void updateProcessDefinition(List<LocalParams> local,String jobName, String projectName,String loginUser, String loginPassword, String hostName,Result result);

}

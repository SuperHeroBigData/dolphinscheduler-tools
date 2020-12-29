package iquantex.com.upgrade;

import iquantex.com.dolphinscheduler.api.Authenticator;
import iquantex.com.dolphinscheduler.api.impl.AuthenticatorImpl;
import iquantex.com.dolphinscheduler.api.impl.SchedulerImpl;
import iquantex.com.dolphinscheduler.api.impl.WorkFlowModelImpl;
import iquantex.com.dolphinscheduler.pojo.ProcessDefinition;
import iquantex.com.dolphinscheduler.pojo.Result;
import iquantex.com.dolphinscheduler.pojo.Schedule;
import iquantex.com.easyexcel.SheetEnv;

/**
 * @ClassName BuildTask
 * @Description TODO 构建任务
 * @Author jianping.mu
 * @Date 2020/11/26 11:10 上午
 * @Version 1.0
 */
public class BuildTask {

    private final SheetEnv login;
    private final Authenticator authenticator = new AuthenticatorImpl();
    public BuildTask(SheetEnv login) {
        this.login = login;
    }

    public Result createWork(ProcessDefinition processDefinition){
        return new WorkFlowModelImpl(login,authenticator).createWorkFlow(processDefinition);
    }

    public Result updateWork(ProcessDefinition processDefinition){
        return new WorkFlowModelImpl(login,authenticator).updateProcessDefinition(processDefinition);
    }

    public Result batchDeleteWork(String taskName){
        return new WorkFlowModelImpl(login,authenticator).deleteProcessDefinition(taskName);
    }

    public Result createWorkSchedule(Schedule schedule){
        return new SchedulerImpl(login,authenticator).createSchedule(schedule);
    }

    public Result updateWorkSchedule(Schedule schedule){
        return new SchedulerImpl(login,authenticator).updateSchedule(schedule);
    }

}

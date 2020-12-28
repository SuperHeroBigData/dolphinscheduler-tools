package iquantex.com.upgrade;

import iquantex.com.dolphinscheduler.command.impl.SchedulerImpl;
import iquantex.com.dolphinscheduler.command.impl.WorkFlowModelImpl;
import iquantex.com.dolphinscheduler.pojo.ProcessDefinition;
import iquantex.com.dolphinscheduler.pojo.Result;
import iquantex.com.dolphinscheduler.pojo.Schedule;
import iquantex.com.entity.SheetEnv;

import java.util.List;

/**
 * @ClassName BuildTask
 * @Description TODO 构建任务
 * @Author jianping.mu
 * @Date 2020/11/26 11:10 上午
 * @Version 1.0
 */
public class BuildTask {

    private final SheetEnv login;

    public BuildTask(SheetEnv login) {
        this.login = login;
    }

    public Result createWork(ProcessDefinition processDefinition){
        return new WorkFlowModelImpl(login).createWorkFlow(processDefinition);
    }

    public Result updateWork(ProcessDefinition processDefinition){
        return new WorkFlowModelImpl(login).updateProcessDefinition(processDefinition);
    }

    public Result batchDeleteWork(String taskName){
        return new WorkFlowModelImpl(login).deleteProcessDefinition(taskName);
    }

    public Result createWorkSchedule(Schedule schedule){
        return new SchedulerImpl(login).createSchedule(schedule);
    }

    public Result updateWorkSchedule(Schedule schedule){
        return new SchedulerImpl(login).updateSchedule(schedule);
    }

}

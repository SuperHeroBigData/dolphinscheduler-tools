package iquantex.com.upgrade;

import iquantex.com.dolphinscheduler.command.ScheduleModel;
import iquantex.com.dolphinscheduler.command.WorkFlowModel;
import iquantex.com.dolphinscheduler.command.impl.SchedulerImpl;
import iquantex.com.dolphinscheduler.command.impl.WorkFlowModelImpl;
import iquantex.com.dolphinscheduler.pojo.ProcessDefinition;
import iquantex.com.dolphinscheduler.pojo.Result;
import iquantex.com.dolphinscheduler.pojo.Schedule;
import iquantex.com.entity.SheetEnv;

/**
 * @ClassName BuildTask
 * @Description TODO 构建任务
 * @Author jianping.mu
 * @Date 2020/11/26 11:10 上午
 * @Version 1.0
 */
public class BuildTask {

    private final WorkFlowModel workFlowModel;
    private final ScheduleModel scheduleModel;
    private final SheetEnv login;

    public BuildTask(SheetEnv login) {
        this.workFlowModel = new WorkFlowModelImpl(login);
        this.scheduleModel = new SchedulerImpl(login);
        this.login = login;
    }

    public Result getCreateWorkStat(ProcessDefinition processDefinition){
        return workFlowModel.createWorkFlow(login,processDefinition);
    }

    public Result getCreateWorkSchedule(Schedule schedule){
        return scheduleModel.createSchedule(schedule);
    }


}

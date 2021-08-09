package iquantex.com.permission.impl;

import com.alibaba.fastjson.JSONObject;
import iquantex.com.easyexcel.CommonConfig;
import iquantex.com.entity.AbstractParameters;
import iquantex.com.entity.Parameters;
import iquantex.com.easyexcel.SheetParam;
import iquantex.com.entity.ParentTask;
import iquantex.com.entity.TimeOut;
import iquantex.com.enums.Priority;
import iquantex.com.enums.TaskType;
import iquantex.com.utils.Constant;
import iquantex.com.utils.Constants;
import netscape.javascript.JSObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static iquantex.com.dolphinscheduler.utils.RandomUtil.taskId;

/**
 * @ClassName Task
 * @Description TODO
 * @Author jianping.mu
 * @Date 2020/11/30 7:33 下午
 * @Version 1.0
 */
abstract class AbstractTask {
    private final SheetParam sheet;
    private final ParentTask parentTask;
    private final CommonConfig commonConfig;
    public AbstractTask(SheetParam sheet, ParentTask parentTask) {
        this.parentTask = parentTask;
        this.sheet = sheet;
        CommonConfig config =sheet.getCommonConfig();
        this.commonConfig =config;
    }

    public ParentTask convertToData() {
        String taskId = taskId(Constants.RANDOM_ID);
        parentTask.setId(taskId);
        parentTask.setDescription(sheet.getDescription());
        parentTask.setType(TaskType.SHELL.name());
        parentTask.setName(sheet.getSubApplication());
        parentTask.setMaxRetryTimes(Integer.parseInt(commonConfig.getMaxRetryTimes()));
        parentTask.setRunFlag(commonConfig.getRunFlag());
        parentTask.setRetryInterval(commonConfig.getRetryInterval());
        parentTask.setTaskInstancePriority(Priority.valueOf(commonConfig.getTaskInstancePriority()));
        parentTask.setWorkerGroup(commonConfig.getWorkerGroup());
        parentTask.setTimeout(commonConfig.getTime_out());
        parentTask.setPreTasks(Arrays.asList(commonConfig.getPretasksList().split(",")));
        return parentTask;
    }

    public List<String> getPreTask(){
        List<String> preTasks = new ArrayList<>();
       preTasks= Arrays.asList(commonConfig.getPretasksList().split(","));
        return preTasks;
    }

}



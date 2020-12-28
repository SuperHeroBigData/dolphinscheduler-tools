package iquantex.com.permission.impl;

import iquantex.com.entity.Parameters;
import iquantex.com.entity.SheetParam;
import iquantex.com.entity.TimeOut;
import iquantex.com.enums.TaskType;
import iquantex.com.utils.Constant;

import java.util.ArrayList;
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
    private final Parameters parameters;

    public AbstractTask(SheetParam sheet, Parameters parameters) {
        this.parameters = parameters;
        this.sheet = sheet;
    }

    public Parameters convertToData() {
        String taskId = taskId(Constant.RANDOM_ID);
        parameters.setId(taskId);
        parameters.setDescription(sheet.getDescription());
        parameters.setMaxRetryTimes(Objects.isNull(sheet.getMaxRerun())
                ? Constant.MAX_RETRY_TIMES : Integer.parseInt(sheet.getMaxRerun()));
        TimeOut timeOut = new TimeOut();
        timeOut.setInterval(Long.parseLong(sheet.getAlarmTime()));
        parameters.setTimeout(timeOut);
        return parameters;
    }

    public List<String> getPreTask(String taskName){
        List<String> preTasks = new ArrayList<>();

        /**
         * 添加依赖检查依赖
         */
        if (Objects.nonNull(sheet.getDependType()) && Objects.equals(TaskType.DEPENDENT,TaskType.valueOf(sheet.getDependType()))){
            preTasks.add(taskName+"_"+TaskType.DEPENDENT.name());
        }

        /**
         * 添加shell任务依赖
         */
        if (Objects.nonNull(sheet.getTaskPath()) && Objects.nonNull(sheet.getScriptType())){
            preTasks.add(taskName+"_"+sheet.getScriptType());
        }
        return preTasks;
    }

}



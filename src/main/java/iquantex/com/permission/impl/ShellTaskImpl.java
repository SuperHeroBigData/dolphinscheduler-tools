package iquantex.com.permission.impl;

import com.alibaba.fastjson.JSONObject;
import iquantex.com.entity.LocalParams;
import iquantex.com.entity.SheetParam;
import iquantex.com.entity.TimeOut;
import iquantex.com.entity.shell.ShellParameters;
import iquantex.com.entity.shell.ShellParams;
import iquantex.com.enums.TaskType;
import iquantex.com.utils.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ShellTaskImpl
 * @Description TODO
 * @Author jianping.mu
 * @Date 2020/11/17 4:27 下午
 * @Version 1.0
 */
public class ShellTaskImpl extends AbstractTask {
    private final SheetParam sheet;
    private ShellParameters shellParameters;
    private final ShellParams params;
    private final boolean flag;

    public ShellTaskImpl(SheetParam sheet, List<LocalParams> localParamsList, ShellParameters shellParameters, boolean flag) {
        super(sheet, shellParameters);
        this.shellParameters = shellParameters;
        this.params = new ShellParams();
        this.params.setLocalParams(localParamsList);
        this.sheet = sheet;
        this.flag = flag;
    }

    /**
     * 封装shell类型参数
     *
     * @return
     */
    @Override
    public ShellParameters convertToData() {
        shellParameters = (ShellParameters) super.convertToData();
        shellParameters.setDependence(new JSONObject());
        shellParameters.setType(TaskType.SHELL.name());
        String taskName = sheet.getSubApplication() + "." + sheet.getTableName();
        shellParameters.setName(taskName + "_" + sheet.getScriptType());
        shellParameters.setMaxRetryTimes(Constant.MAX_RETRY_TIMES);

        if (flag) {
            List<String> preTask = super.getPreTask(taskName);
            shellParameters.setPreTasks(preTask);
        } else {
            shellParameters.setPreTasks(new ArrayList<>());
        }

        params.setResourceList(new ArrayList<>());
        params.setRawScript(sheet.getTaskPath());


        shellParameters.setParams(params);
        TimeOut timeOut = new TimeOut();
        timeOut.setInterval(Long.parseLong(sheet.getAlarmTime()));
        shellParameters.setTimeout(timeOut);
        return shellParameters;
    }

}

package iquantex.com.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import iquantex.com.dolphinscheduler.pojo.ProcessDefinition;
import iquantex.com.dolphinscheduler.pojo.Result;
import iquantex.com.dolphinscheduler.pojo.Schedule;
import iquantex.com.entity.LocalParams;
import iquantex.com.entity.SheetEnv;
import iquantex.com.enums.DDL;
import iquantex.com.upgrade.BuildTask;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static iquantex.com.easyexcel.ExcelListener.getSheetEnvList;
import static iquantex.com.utils.HttpUtil.executeResult;
import static iquantex.com.utils.HttpUtil.sendWeChatRobotTalkRisk;

/**
 * @ClassName ParamUtils
 * @Description TODO
 * @Author jianping.mu
 * @Date 2020/11/24 5:21 下午
 * @Version 1.0
 */
public class ParamUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParamUtils.class);

    private static SheetEnv sheetEnv = null;

    /**
     * 参数格式转换
     *
     * @param taskParam key=value
     * @return json
     */
    public static List<LocalParams> taskParamToList(String taskParam) {
        List<LocalParams> listLocalParams = new ArrayList<>();
        LocalParams localParams = null;
        for (String dependent :
                taskParam.split("\n")) {
            String[] keyValue = dependent.replace("_x000D_","").split("=");
            localParams = new LocalParams();
            localParams.setProp(keyValue[0]);
            localParams.setValue(keyValue[1]);
            listLocalParams.add(localParams);
        }
        return listLocalParams;
    }

    /**
     * 解析依赖字段信息
     * @param dependentParam
     * @return
     */
    public static List<String> taskDependentToList(String dependentParam){

        return Objects.isNull(dependentParam) ? new ArrayList<>() : new ArrayList<>(Arrays.asList(dependentParam.replace("_x000D_","").replaceAll("\n+","\n").split("\n")));
    }

    /**
     * 获取当前环境
     *
     * @return
     */
    public static SheetEnv getEnvInfo() {
        List<SheetEnv> envList = getSheetEnvList();
        if (CollectionUtils.isEmpty(envList)) {
            throw new RuntimeException("环境队列为空。");
        }
        String present = null;
        for (SheetEnv sheetEnv : envList
        ) {
            if (Objects.isNull(present)) {
                present = sheetEnv.getPresent();
            }

            if (Objects.nonNull(present) &&
                    Objects.equals(present, sheetEnv.getEnvironment())) {
                return sheetEnv;
            }
        }
        return null;
    }

    /**
     * 获取当前环境
     *
     * @return
     */
    public static SheetEnv getInstanceEnv() {
        if (Objects.isNull(sheetEnv)) {
            sheetEnv = getEnvInfo();
            JSONObject json = (JSONObject) JSON.toJSON(sheetEnv);
            assert json != null;
            json.put("password","******");
            json.put("dbPassword","******");
            LOGGER.info("获取环境信息：{}", json);
        }

        return sheetEnv;
    }

    /**
     * 根据类型提交作业
     * @param processDefinition
     * @return
     */
    public static void commitTask(ProcessDefinition processDefinition){
        BuildTask buildTask = new BuildTask(getInstanceEnv());
        Result result= null;
        switch (DDL.valueOf(getInstanceEnv().getJobDDL().toUpperCase())) {
            case CREATE:
                result = buildTask.createWork(processDefinition);
                result.setJobName(processDefinition.getName());
                executeResult(result);
                break;
            case DELETE:
                result = buildTask.batchDeleteWork(processDefinition.getName());
                result.setJobName(processDefinition.getName());
                executeResult(result);
                break;
            case UPDATE:
                result = buildTask.updateWork(processDefinition);
                result.setJobName(processDefinition.getName());
                executeResult(result);
                break;
            default:
                LOGGER.error("执行方式错误!!! " + getInstanceEnv().getJobDDL());
                throw new IllegalArgumentException("执行方式错误!!! " + getInstanceEnv().getJobDDL());
        }
    }

    /**
     * 根据类型定时任务
     * @param schedule
     * @return
     */
    public static void commitSchedule(Schedule schedule){
        BuildTask buildTask = new BuildTask(getInstanceEnv());
        switch (DDL.valueOf(getInstanceEnv().getJobDDL().toUpperCase())) {
            case CREATE:
                executeResult(buildTask.createWorkSchedule(schedule));
                break;
            case UPDATE:
                executeResult(buildTask.createWorkSchedule(schedule));
                break;
            case DELETE:
                //TODO 删除定时任务
                break;
            default:
                LOGGER.error("执行方式错误!!! " + getInstanceEnv().getJobDDL());
                throw new IllegalArgumentException("执行方式错误!!! " + getInstanceEnv().getJobDDL());
        }
    }
}

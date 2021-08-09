package iquantex.com.permission.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import iquantex.com.dolphinscheduler.pojo.LocalParams;
import iquantex.com.dolphinscheduler.pojo.ProcessDefinition;
import iquantex.com.easyexcel.SheetParam;
import iquantex.com.entity.*;
import iquantex.com.entity.dependent.DependParameters;
import iquantex.com.entity.shell.ShellParameters;
import iquantex.com.entity.stroedprodure.StoredProcedureParameters;
import iquantex.com.enums.TaskType;
import iquantex.com.process.Property;
import iquantex.com.upgrade.InstanceTask;
import iquantex.com.utils.Constant;
import iquantex.com.utils.ParamUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static iquantex.com.dolphinscheduler.utils.RandomUtil.randomInteger;
import static iquantex.com.utils.ParamUtils.commitTask;

/**
 * @ClassName SheetParamConvert
 * @Description TODO
 * @Author jianping.mu
 * @Date 2020/11/28 2:10 下午
 * @Version 1.0
 * 解析excel to task
 */
public class ParamConvert {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParamConvert.class);
    private List<Property> localParamsList;
    private final SheetParam sheetParam;
    private boolean flag = true;
    private final JSONObject locations;
    private final JSONArray taskTypeArr;
    private final List<Connects> connectsList;
    private final StringBuffer dependenceIdAll;

    public ParamConvert(SheetParam sheetParam) {
        this.sheetParam = sheetParam;
        this.taskTypeArr = new JSONArray();
        this.locations = new JSONObject();
        this.connectsList = new ArrayList<>();
        this.dependenceIdAll = new StringBuffer(5);
        call();
    }

    /**
     * 解析excel对象信息
     */
    public void call() {
        LOGGER.info("开始解析excel中的任务，任务名为:{}",sheetParam.getSubApplication());
        String taskParam = sheetParam.getTaskParam();
        if (Objects.nonNull(taskParam)) {
            localParamsList = ParamUtils.taskParamToList(taskParam.replaceAll("\n+","\n"));
        }
        Connects connects = new Connects();
        List<HashMap<String, Boolean>> targetarr = new ArrayList<>();
        String taskType = sheetParam.getTaskType();
        analysisType(taskType, connects, targetarr);
        String dependentId = null;
        String dependentName = null;
        //遍历任务的依赖，封装location参数
        for (Map<String, Boolean> tar :
                targetarr) {
            for (String key : tar.keySet()) {
                boolean value = tar.get(key);
                String[] taskInfo = key.split("\\|");
                String taskId = taskInfo[0];
                String taskName = taskInfo[1];
                if (value) {
                    dependentId = taskId;
                    dependentName = taskName;
                } else {
                    dependenceIdAll.append(taskId).append(",");
                    locations.fluentPutAll(getLocation(taskId, taskName, false));
                }
            }
        }

        locations.fluentPutAll(getLocation(dependentId, dependentName, true));
    }
    /**
     * 封装工作流
     */
    public  void packageWorkFlow()
    {
        setTaskParameters();
        new SubProcessTaskImpl(sheetParam).convertToData();
    }

    /**
     * 设置task任务参数
     */
    public void setTaskParameters() {
            TaskParameters taskParameters = new TaskParameters();
            taskParameters.setGlobalParams(new ArrayList<>());
            taskParameters.setTasks(taskTypeArr);
            taskParameters.setTenantId(new InstanceTask().getTenantId());
            taskParameters.setTimeout(sheetParam.getAlarmTime());

            String jsonString = JSONObject.toJSONString(taskParameters, SerializerFeature.WriteMapNullValue);
            String workflowName = sheetParam.getSubApplication() + "." + sheetParam.getTableName();
            aggregateTask(jsonString, workflowName);
    }

    /**
     * 提交task到ds
     *
     * @param json 定义工作流
     * @param taskName 工作流名称
     * @return
     */
    public void aggregateTask(String json, String taskName) {
        try {
            LOGGER.info("【aggregateTask】提交当前任务："+taskName);
            ProcessDefinition processDefinition = new ProcessDefinition();
            processDefinition.setConnects(JSONObject.toJSONString(connectsList));
            processDefinition.setDescription(sheetParam.getDescription());
            processDefinition.setGlobalParams("[]");
            processDefinition.setName(taskName);
            processDefinition.setProcessDefinitionJson(json);
            processDefinition.setLocations(locations.toJSONString());
            commitTask(processDefinition);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 执行不同分支
     *
     * @param type      任务分类
     * @param connects  任务参数
     * @param targetarr 任务画布参数
     */
    public void analysisType(String type, Connects connects, List<HashMap<String, Boolean>> targetarr) {
        HashMap<String, Boolean> map = new HashMap<>(3);
        switch (TaskType.valueOf(type.toUpperCase())) {
            case SHELL:
                ParentTask parentTask = new ShellTaskImpl(sheetParam,
                        localParamsList, new ParentTask(), flag).convertToData();
                String shellId = parentTask.getId();
                String shellName = parentTask.getName();
                map.put(shellId + "|" + shellName, flag);
                getLocationConnect(connects, shellId);
                taskTypeArr.fluentAdd(JSONArray.toJSON(parentTask));
                break;
            case DEPENDENT:
                DependParameters dependParameters = new DependentTaskImpl(sheetParam,
                        new DependParameters(),flag).convertToData();
                String dependId = dependParameters.getId();
                String dependName = dependParameters.getName();
                map.put(dependId + "|" + dependName, false);
                getLocationConnect(connects, dependId);
                taskTypeArr.fluentAdd(JSONArray.toJSON(dependParameters));
                break;
            case PROCEDURE:
                StoredProcedureParameters storedProcedureParameters = new StoreProducerTaskImpl(sheetParam,
                        localParamsList, new StoredProcedureParameters(), flag).convertToData();
                String procedureId = storedProcedureParameters.getId();
                String procedureName = storedProcedureParameters.getName();
                map.put(procedureId + "|" + procedureName, flag);
                getLocationConnect(connects, procedureId);
                taskTypeArr.fluentAdd(JSONArray.toJSON(storedProcedureParameters));
                break;
            case PROCEDURE:
                StoredProcedureParameters storedProcedureParameters = new StoreProducerTaskImpl(sheetParam,
                        localParamsList, new StoredProcedureParameters(), flag).convertToData();
                String procedureId = storedProcedureParameters.getId();
                String procedureName = storedProcedureParameters.getName();
                map.put(procedureId + "|" + procedureName, flag);
                getLocationConnect(connects, procedureId);
                taskTypeArr.fluentAdd(JSONArray.toJSON(storedProcedureParameters));
                break;
            default:
                throw new IllegalArgumentException("该任务类型不存在："+type.toUpperCase());
        }
        targetarr.add(map);
    }

    /**
     * 初始化作业信息 location部分，主要体现在画布中位置和依赖关系
     *
     * @param taskId 任务Id
     * @param taskName 任务名
     * @return location依赖关系
     */
    public JSONObject getLocation(String taskId, String taskName, boolean flag) {
        Location location = new Location();
        JSONObject jsonLocation = new JSONObject();
        if (!flag) {
            location.setNodenumber("1");
        } else {
            location.setTargetarr(dependenceIdAll.substring(0, dependenceIdAll.length() - 1));
        }
        location.setX(randomInteger(Constant.NUMBER));
        location.setY(randomInteger(Constant.NUMBER));
        location.setName(taskName);

        jsonLocation.put(taskId, JSONObject.toJSON(location));
        return jsonLocation;
    }


    /**
     * 设置connect参数
     *
     * @param connects DAG依赖关系
     * @param id 任务Id
     */
    public void getLocationConnect(Connects connects, String id) {
        if (flag) {
            connects.setEndPointTargetId(id);
        } else {
            String endPointTargetId = connects.getEndPointTargetId();
            connects = new Connects();
            connects.setEndPointTargetId(endPointTargetId);
            connects.setEndPointSourceId(id);
            connectsList.add(connects);
        }
    }

}

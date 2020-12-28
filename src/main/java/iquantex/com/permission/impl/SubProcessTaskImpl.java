package iquantex.com.permission.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import iquantex.com.dolphinscheduler.api.exceptions.TasksException;
import iquantex.com.dolphinscheduler.pojo.Cron;
import iquantex.com.dolphinscheduler.pojo.ProcessDefinition;
import iquantex.com.dolphinscheduler.pojo.Result;
import iquantex.com.dolphinscheduler.pojo.Schedule;
import iquantex.com.entity.*;
import iquantex.com.entity.subprocess.Params;
import iquantex.com.entity.subprocess.SubProcessParameters;
import iquantex.com.enums.State;
import iquantex.com.enums.TaskType;
import iquantex.com.permission.TaskCommit;
import iquantex.com.upgrade.BuildTask;
import iquantex.com.upgrade.InstanceTask;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static iquantex.com.dolphinscheduler.utils.RandomUtil.randomInteger;
import static iquantex.com.dolphinscheduler.utils.RandomUtil.taskId;
import static iquantex.com.utils.HttpUtil.executeResult;
import static iquantex.com.utils.ParamUtils.*;

/**
 * @ClassName SubProcessTaskImpl
 * @Description TODO
 * @Author jianping.mu
 * @Date 2020/12/3 3:14 下午
 * @Version 1.0
 */
public class SubProcessTaskImpl implements TaskCommit {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubProcessTaskImpl.class);
    private static final String TASK_NOT_EXIST = "任务不存在";
    private static String CRON = null;
    private SubProcessParameters subProcessParameters;
    private SheetParam sheet;
    private InstanceTask instanceTask;
    private static final JSONObject JSON_LOCATIONS = new JSONObject();
    private static final List<Connects> LIST_CONNECTS = new ArrayList<>();
    private static final List<String> NODE_NUMBER_LIST = new ArrayList<>(10);
    private static final Queue<SubProcessParameters> SUB_PROCESS_PARAMETERS_QUEUE = new LinkedList<>();

    public SubProcessTaskImpl() {
    }

    public SubProcessTaskImpl(SheetParam sheet) {
        this.subProcessParameters = new SubProcessParameters();
        this.instanceTask = new InstanceTask();
        this.sheet = sheet;
    }

    /**
     * 初始化subprocess信息
     */
    public void convertToData() {
        String taskId = taskId(iquantex.com.utils.Constant.RANDOM_ID);
        subProcessParameters.setId(taskId);
        subProcessParameters.setDescription(Objects.isNull(sheet.getDescription()) ? "" : sheet.getDescription());
        subProcessParameters.setMaxRetryTimes(Objects.isNull(sheet.getMaxRerun()) ? iquantex.com.utils.Constant.MAX_RETRY_TIMES : Integer.parseInt(sheet.getMaxRerun()));

        TimeOut timeOut = new TimeOut();
        timeOut.setInterval(Long.parseLong(sheet.getAlarmTime()));
        subProcessParameters.setTimeout(timeOut);

        subProcessParameters.setType(TaskType.SUB_PROCESS.name());
        String taskName = sheet.getSubApplication() + "." + sheet.getTableName();
        subProcessParameters.setName(taskName);

        ProcessDefinition processDefinition = instanceTask.getProcessDefinitionId(taskName, sheet.getApplication());

        if (Objects.isNull(processDefinition.getId())) {
            Result result = new Result();
            result.setState(State.ERROR.name());
            result.setMsg(subProcessParameters.getName() + "\t" + TASK_NOT_EXIST);
            executeResult(result);
            throw new TasksException(subProcessParameters.getName() + "\t" + TASK_NOT_EXIST);
        }

        Params params = new Params();
        params.setProcessDefinitionId(processDefinition.getId());
        subProcessParameters.setParams(params);
        String depend = sheet.getDepend();
        List<String> dependent = taskDependentToList(depend);
        List<String> newDependent = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(dependent) && CollectionUtils.isNotEmpty(SUB_PROCESS_PARAMETERS_QUEUE)) {
            subProcessLocations(taskId, taskName, dependent, newDependent);
        } else {
            JSON_LOCATIONS.fluentPutAll(getLocation(taskId, taskName, null));
        }
        subProcessParameters.setPreTasks(newDependent);
        NODE_NUMBER_LIST.addAll(dependent);

        if (Objects.isNull(CRON)) {
            CRON = sheet.getTaskScheduler();
        }
        SUB_PROCESS_PARAMETERS_QUEUE.add(subProcessParameters);
    }

    /**
     * 将当前作业依赖从队列中获取进行对象映射
     *
     * @param taskId
     * @param taskName
     * @param depend
     */
    public void subProcessLocations(String taskId, String taskName,
                                    List<String> depend, List<String> newDepend) {
        LOGGER.info("开始生成作业依赖：{}", taskName);
        System.out.println(depend);
        StringBuilder locationTarget = new StringBuilder(10);
        Connects connects = null;
        String subProcessName = null;
        String subProcessId = null;
        for (String task :
                depend) {
            connects = new Connects();
            connects.setEndPointTargetId(taskId);
            for (SubProcessParameters subProcess :
                    SUB_PROCESS_PARAMETERS_QUEUE) {
                subProcessName = subProcess.getName();
                if (Objects.equals(task, subProcessName)) {
                    newDepend.add(task);
                    subProcessId = subProcess.getId();

                    connects.setEndPointSourceId(subProcessId);
                    LIST_CONNECTS.add(connects);
                    locationTarget.append(subProcessId).append(",");
                }
            }

            if (Objects.isNull(getTaskId(task))) {
                throw new TasksException(task + " 依赖任务不存在");
            }
        }

        JSONObject location;
        if (locationTarget.length() != 0) {
            location = getLocation(taskId, taskName, locationTarget.substring(0, locationTarget.length() - 1));
        } else {
            location = getLocation(taskId, taskName, null);
        }
        JSON_LOCATIONS.fluentPutAll(location);


    }

    public String getTaskId(String taskName) {
        LOGGER.info("查询依赖信息：{}", taskName);
        ProcessDefinition processDefinitionId = instanceTask.getProcessDefinitionId(taskName, Objects.requireNonNull(getEnvInfo()).getProjectName());
        JSONObject jsonObject = JSON.parseObject(processDefinitionId.getLocations());
        for (String key : jsonObject.keySet()) {
            JSONObject location = jsonObject.getJSONObject(key);
            String nodeName = location.getString("name");
            if (Objects.equals(nodeName, taskName)) {
                return key;
            }
        }
        return null;
    }

    /**
     * 美化坐标轴
     *
     * @return
     */
    public Long point() {
        //TODO x，y轴坐标系
        return null;
    }

    /**
     * 获取nodeNumber
     *
     * @param taskName
     * @return
     */
    public static Long nodeNumber(String taskName) {
        Map<String, Long> collect = NODE_NUMBER_LIST.
                stream().
                collect(Collectors.
                        groupingBy(Function.identity(), Collectors.counting()));
        return Objects.isNull(collect.get(taskName)) ? 0L : collect.get(taskName);
    }

    /**
     * 替换nodeNumber默认值
     */
    public static JSONObject replaceNodeNumber() {
        for (String key :
                JSON_LOCATIONS.keySet()) {
            JSONObject location = JSON_LOCATIONS.getJSONObject(key);
            Long nodeNumber = nodeNumber(location.getString("name"));
            location.put("nodenumber", String.valueOf(nodeNumber));
        }
        return JSON_LOCATIONS;
    }

    /**
     * 封装subProcess依赖 location部分
     *
     * @param taskId
     * @param taskName
     * @return 返回单个task的location参数
     */
    public JSONObject getLocation(String taskId, String taskName, String targetarr) {
        Location location = new Location();
        JSONObject jsonLocation = new JSONObject();
        location.setTargetarr(Objects.isNull(targetarr) ? "" : targetarr);
        location.setX(randomInteger(iquantex.com.utils.Constant.NUMBER));
        location.setY(randomInteger(iquantex.com.utils.Constant.NUMBER));
        location.setName(taskName);
        jsonLocation.put(taskId, JSONObject.toJSON(location));
        return jsonLocation;
    }

    /**
     * 获取依赖对象队列
     *
     * @return
     */
    public static JSONArray getDependenceDefinition() {
        JSONArray dependence = new JSONArray();
        if (CollectionUtils.isEmpty(SUB_PROCESS_PARAMETERS_QUEUE)) {
            throw new TasksException("依赖队列为空。");
        }

        while (!SUB_PROCESS_PARAMETERS_QUEUE.isEmpty()) {
            SubProcessParameters parameters = SUB_PROCESS_PARAMETERS_QUEUE.poll();
            dependence.fluentAdd(JSONObject.toJSON(parameters));
        }
        return dependence;
    }

    /**
     * 按照依赖关系提交任务
     *
     * @param processDefinition
     * @return 返回执行结果
     */
    @Override
    public void getTaskParam(ProcessDefinition processDefinition) {

        JSONObject jsonObject = replaceNodeNumber();
        createJob(processDefinition);
        LOGGER.info("依赖关系locations参数：{}", jsonObject.toJSONString());
        processDefinition.setLocations(jsonObject.toJSONString());
        commitTask(processDefinition);
        SheetEnv instanceEnv = getInstanceEnv();
        Schedule taskSchedule = getTaskSchedule(instanceEnv, processDefinition.getName());
        LOGGER.info("Job定时信息：{}", JSONObject.toJSONString(taskSchedule));
        commitSchedule(taskSchedule);
    }

    /**
     * 创建新任务
     *
     * @param processDefinition
     */
    public void createJob(ProcessDefinition processDefinition) {
        TaskParameters taskParameters = new TaskParameters();
        taskParameters.setGlobalParams(new ArrayList<>());
        JSONArray dependenceDefinition = getDependenceDefinition();

        LOGGER.info("依赖关系tasks参数：{}", dependenceDefinition);
        taskParameters.setTasks(dependenceDefinition);
        taskParameters.setTenantId(new InstanceTask().getTenantId());
        taskParameters.setTimeout(iquantex.com.utils.Constant.TIMEOUT);

        String jsonString = JSONObject.toJSONString(taskParameters, SerializerFeature.WriteMapNullValue);


        LOGGER.info("依赖关系connect参数：{}", JSONObject.toJSONString(LIST_CONNECTS));
        processDefinition.setConnects(JSONObject.toJSONString(LIST_CONNECTS));

        processDefinition.setDescription("");

        processDefinition.setGlobalParams("[]");
        processDefinition.setProcessDefinitionJson(jsonString);
    }

    /**
     * Job定时
     *
     * @param instanceEnv
     * @param jobName
     * @return
     */
    public Schedule getTaskSchedule(SheetEnv instanceEnv, String jobName) {
        Schedule schedule = new Schedule();
        ProcessDefinition processDefinition = new InstanceTask().getProcessDefinitionId(jobName, instanceEnv.getProjectName());
        Long id = processDefinition.getId();
        schedule.setProcessDefinitionId(String.valueOf(id));
        schedule.setProjectName(instanceEnv.getProjectName());

        /*邮件*/
        schedule.setReceivers(instanceEnv.getReceivers());
        schedule.setReceiversCc(instanceEnv.getReceiversCc());

        /*cron表达式*/
        Cron cron = new Cron();
        DateTime startTime = DateTime.now();
        startTime = new DateTime(startTime.year().get(),
                startTime.monthOfYear().get(),
                startTime.dayOfMonth().get(),
                startTime.hourOfDay().get(),
                startTime.minuteOfHour().get(),
                0);
        cron.setStartTime(startTime.toDate());
        cron.setCrontab(CRON);
        schedule.setSchedule(cron);
        return schedule;
    }
}

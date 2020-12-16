package iquantex.com.dolphinscheduler.command;

/**
 * @author mujp
 */
public interface Constant {

    String START_JOB_CONN = "/dolphinscheduler/projects/${projectName}/executors/start-process-instance";
    String STATE ="/dolphinscheduler/projects/${projectName}/process/release";
    String WORK_FLOW = "/dolphinscheduler/projects/${projectName}/process/save";
    String PROCESS_UPDATE = "/dolphinscheduler/projects/${projectName}/process/update";
    String LOG_INTERFACE_CONN = "/dolphinscheduler/log/detail";
    String CREATE_SCHEDULE = "/dolphinscheduler/projects/${projectName}/schedule/create";
    String OFFLINE_SCHEDULE = "/dolphinscheduler/projects/${projectName}/schedule/offline";
    String ONLINE_SCHEDULE = "/dolphinscheduler/projects/${projectName}/schedule/online";
    String UPDATE_SCHEDULE = "/dolphinscheduler/projects/${projectName}/schedule/update";
    String AGREEMENT = "http";
    String SLASH = "//";
    String MARK = ":";
    String URL_HEADER = AGREEMENT + MARK +SLASH;
    String STATE_ERROR = "ERROR";
    String STATE_SUCCESS = "SUCCESS";

    String GET = "get";
    String POST = "post";

    String CONNECTS = "${[]}";

    String LOCATIONS = "{\"${tasks}\":{\"name\":\"${jobName}\",\"targetarr\":\"\",\"x\":${x},\"y\":${y}}}";

    String PROCESS_DEFINITION_JSON = "{\"globalParams\":[],\"tasks\":[{\"type\":\"${type}\",\"id\":\"${tasks}\",\"name\":\"${jobName}\",\"params\":{\"resourceList\":[],\"localParams\":${localParams},\"rawScript\":\"echo \\\"执行 ${projectName} ${jobName}\\\"\\n${command} ${params}\"},\"description\":\"\",\"runFlag\":\"NORMAL\",\"dependence\":{},\"maxRetryTimes\":\"0\",\"retryInterval\":\"1\",\"timeout\":{\"strategy\":\"\",\"interval\":null,\"enable\":false},\"taskInstancePriority\":\"MEDIUM\",\"workerGroupId\":1,\"preTasks\":[]}],\"tenantId\":-1,\"timeout\":0}";

}

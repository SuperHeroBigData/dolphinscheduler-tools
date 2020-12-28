package iquantex.com.dolphinscheduler.command;

/**
 * @author mujp
 */
public interface Constant {

    String START_JOB_CONN = "/dolphinscheduler/projects/${projectName}/executors/start-process-instance";
    String STATE ="/dolphinscheduler/projects/${projectName}/process/release";
    String WORK_FLOW = "/dolphinscheduler/projects/${projectName}/process/save";
    String PROCESS_UPDATE = "/dolphinscheduler/projects/${projectName}/process/update";
    String PROCESS_DELETE = "/dolphinscheduler/projects/${projectName}/process/delete";

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

    String MSG = "msg";
    String ONLINE = "1";
    String OFFLINE = "0";

}

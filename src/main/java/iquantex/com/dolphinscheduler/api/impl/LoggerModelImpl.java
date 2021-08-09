package iquantex.com.dolphinscheduler.api.impl;


import iquantex.com.dolphinscheduler.utils.HttpClient;
import iquantex.com.dolphinscheduler.api.Authenticator;
import iquantex.com.dolphinscheduler.api.Constant;
import iquantex.com.dolphinscheduler.api.LoggerModel;
import iquantex.com.dolphinscheduler.pojo.Login;
import iquantex.com.dolphinscheduler.pojo.Result;
import iquantex.com.dolphinscheduler.mapper.ProcessInstanceMapper;
import iquantex.com.dolphinscheduler.utils.DBManager;
import iquantex.com.easyexcel.SheetEnv;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mujp
 */
public class LoggerModelImpl implements LoggerModel {
    protected static final Log logger = LogFactory.getLog(LoggerModelImpl.class);

    @Override
    public Result logClientService(Login login,String taskName) {
        Authenticator authenticator = new AuthenticatorImpl();
        Result result = authenticator.authenticate(new SheetEnv());
        if (result.getData() == null) {
            result.setState(Constant.STATE_ERROR);
            result.setMsg("未获取到session，认证失败");
            return result;
        }
        ProcessInstanceMapper processInstanceMapper = DBManager.processInstanceMapper();
        String taskInstId = processInstanceMapper.queryTaskInstIdByJobName(taskName, login.getProjectName());
        if (taskInstId == null) {
            result.setMsg("获取元数据taskInstId不存在。");
            throw new IllegalArgumentException("获取元数据信息不存在！！！");
        }
        logger.info("获取元数据信息: "+taskInstId);
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("limit", "200000"));
        parameters.add(new BasicNameValuePair("skipLineNum", "0"));
        parameters.add(new BasicNameValuePair("taskInstId", taskInstId));
        HttpClient httpClient = new HttpClient(parameters, login.getHostName()+ Constant.LOG_INTERFACE_CONN, result.getData(), Constant.GET);
        httpClient.submit(result);
        return result;
    }

}

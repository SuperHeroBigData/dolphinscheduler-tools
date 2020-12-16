package iquantex.com.dolphinscheduler.command.impl;

import com.alibaba.fastjson.JSONObject;
import iquantex.com.dolphinscheduler.api.common.HttpClient;
import iquantex.com.dolphinscheduler.command.Authenticator;
import iquantex.com.dolphinscheduler.command.Constant;
import iquantex.com.dolphinscheduler.command.ScheduleModel;
import iquantex.com.dolphinscheduler.mapper.ProcessInstanceMapper;
import iquantex.com.dolphinscheduler.pojo.*;
import iquantex.com.dolphinscheduler.utils.DBManager;
import iquantex.com.entity.SheetEnv;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName SchedulerImpl
 * @Description TODO
 * @Author jianping.mu
 * @Date 2020/12/15 2:35 下午
 * @Version 1.0
 */
public class SchedulerImpl implements ScheduleModel {
    protected static final Log logger = LogFactory.getLog(SchedulerImpl.class);
    private static final String MSG = "msg";
    private final Authenticator authenticator;
    private final SheetEnv sheetEnv;

    public SchedulerImpl(SheetEnv sheetEnv) {
        this.sheetEnv = sheetEnv;
        this.authenticator = new AuthenticatorImpl();
    }

    public Result getSessionId() {
        return authenticator.authenticate(sheetEnv);
    }

    @Override
    public Result createSchedule(Schedule schedule) {
        CloseableHttpResponse response = null;
        String content = null;
        CloseableHttpClient httpclient = null;
        Result result = new Result();
        try {
            httpclient = HttpClients.createDefault();
            String hostName = sheetEnv.getIp() + ":" + sheetEnv.getPort();
            HttpPost httpPost = new HttpPost(Constant.URL_HEADER + hostName + Constant.CREATE_SCHEDULE.replace("${projectName}", sheetEnv.getProjectName()));
            httpPost.setHeader("sessionId", getSessionId().getData());
            List<NameValuePair> parameters = new ArrayList<>();
            parameters.add(new BasicNameValuePair("processDefinitionId", schedule.getProcessDefinitionId()));
            parameters.add(new BasicNameValuePair("schedule", JSONObject.toJSONString(schedule.getSchedule())));
            parameters.add(new BasicNameValuePair("warningType", String.valueOf(WarningType.FAILURE)));
            parameters.add(new BasicNameValuePair("warningGroupId", ""));
            parameters.add(new BasicNameValuePair("failureStrategy", String.valueOf(FailureStrategy.CONTINUE)));
            parameters.add(new BasicNameValuePair("receivers", schedule.getReceivers()));
            parameters.add(new BasicNameValuePair("receiversCc", schedule.getReceiversCc()));
            parameters.add(new BasicNameValuePair("workerGroupId", ""));
            parameters.add(new BasicNameValuePair("processInstancePriority", String.valueOf(Priority.MEDIUM)));

            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters, "UTF-8");
            httpPost.setEntity(formEntity);
            response = httpclient.execute(httpPost);
            content = EntityUtils.toString(response.getEntity(), "UTF-8");
            JSONObject createJobResult = JSONObject.parseObject(content);
            if (Constant.STATE_SUCCESS.equalsIgnoreCase(createJobResult.get(MSG).toString())) {
                ProcessInstanceMapper instanceMapper = DBManager.setUp(result);
                long schedulerId = instanceMapper.getSchedulerId(schedule.getProcessDefinitionId());
                result = onlineSchedule(schedulerId, sheetEnv.getProjectName());
            } else {
                result.setState(Constant.STATE_ERROR);
                result.setMsg(createJobResult.get(MSG).toString());
            }
        } catch (ClientProtocolException e) {
            logger.error("【createSchedule】客户端连接异常：" + e);
            result.setState("error");
            result.setMsg("【createSchedule】客户端连接异常：" + e);
        } catch (IOException e) {
            result.setState(Constant.STATE_ERROR);
            logger.error("【createSchedule】客户端IO异常：" + e);
            result.setMsg("【createSchedule】客户端连接异常：" + e);
        } catch (Exception e) {
            result.setState(Constant.STATE_ERROR);
            result.setMsg("【createSchedule】连接异常：" + e);
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    logger.error("【createSchedule】关闭response响应异常：" + e);
                }
            }
            try {
                if (httpclient != null) {
                    httpclient.close();
                }
            } catch (IOException e) {
                logger.error("【createSchedule】关闭客户端异常：" + e);
            }
        }
        return result;
    }

    @Override
    public Result onlineSchedule(long scheduleId, String projectName) {
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("id", String.valueOf(scheduleId)));
        HttpClient httpClient = new HttpClient(parameters, sheetEnv.getIp() + ":" + sheetEnv.getPort() + Constant.ONLINE_SCHEDULE.replace("${projectName}",
                sheetEnv.getProjectName()), getSessionId().getData(), Constant.POST);
        Result result = new Result();
        System.out.println(result);
        httpClient.submit(result);
        return result;
    }

    @Override
    public Result offlineSchedule(long scheduleId, String projectName) {
        return null;
    }

    @Override
    public Result updateSchedule(Schedule schedule) {
        return null;
    }
}

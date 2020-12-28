package iquantex.com.dolphinscheduler.command.impl;

import com.alibaba.fastjson.JSONObject;
import iquantex.com.dolphinscheduler.api.common.HttpClient;
import iquantex.com.dolphinscheduler.command.Authenticator;
import iquantex.com.dolphinscheduler.command.Constant;
import iquantex.com.dolphinscheduler.command.WorkFlowModel;
import iquantex.com.dolphinscheduler.pojo.*;
import iquantex.com.dolphinscheduler.mapper.ProcessInstanceMapper;
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
 * @author mujp
 */
public class WorkFlowModelImpl implements WorkFlowModel {
    protected static final Log logger = LogFactory.getLog(WorkFlowModelImpl.class);
    private final Authenticator authenticator;
    private final SheetEnv sheetEnv;

    public WorkFlowModelImpl(SheetEnv sheetEnv) {
        this.sheetEnv = sheetEnv;
        this.authenticator = new AuthenticatorImpl();
    }

    public Result getSessionId() {
        return authenticator.authenticate(sheetEnv);
    }

    @Override
    public Result createWorkFlow(ProcessDefinition processDefinitionJson) {

        CloseableHttpResponse response = null;
        String content = null;
        CloseableHttpClient httpclient = null;
        Result result = new Result();
        result.setProjectName(sheetEnv.getProjectName());
        result.setJobName(processDefinitionJson.getName());
        try {
            httpclient = HttpClients.createDefault();
            String hostName = sheetEnv.getIp() + ":" + sheetEnv.getPort();
            HttpPost httpPost = new HttpPost(Constant.URL_HEADER + hostName + Constant.WORK_FLOW.replace("${projectName}", sheetEnv.getProjectName()));
            httpPost.setHeader("sessionId", getSessionId().getData());
            List<NameValuePair> parameters = new ArrayList<>();
            parameters.add(new BasicNameValuePair("connects", processDefinitionJson.getConnects()));
            parameters.add(new BasicNameValuePair("description", processDefinitionJson.getDescription()));
            parameters.add(new BasicNameValuePair("locations", processDefinitionJson.getLocations()));
            parameters.add(new BasicNameValuePair("processDefinitionJson", processDefinitionJson.getProcessDefinitionJson()));
            parameters.add(new BasicNameValuePair("projectName", sheetEnv.getProjectName()));
            parameters.add(new BasicNameValuePair("name", processDefinitionJson.getName()));
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters, "UTF-8");
            httpPost.setEntity(formEntity);
            response = httpclient.execute(httpPost);
            content = EntityUtils.toString(response.getEntity(), "UTF-8");
            JSONObject createJobResult = JSONObject.parseObject(content);
            if (Constant.STATE_SUCCESS.equalsIgnoreCase(createJobResult.get(Constant.MSG).toString())) {
                ProcessInstanceMapper instanceMapper = DBManager.processInstanceMapper();
                ProcessDefinition processDefinition = instanceMapper.queryProcessDefinitionId(processDefinitionJson.getName(), sheetEnv.getProjectName());
                String processDefinitionId = processDefinition.getId().toString();
                LineState lineState = new LineState();
                lineState.setFlag(Constant.ONLINE);
                lineState.setJobName(processDefinitionJson.getName());
                lineState.setProjectName(sheetEnv.getProjectName());
                lineState.setProcessDefinitionId(processDefinitionId);
                releaseState(lineState, result, hostName);
            } else {
                result.setState(Constant.STATE_ERROR);
                result.setMsg(createJobResult.get(Constant.MSG).toString());

            }
        } catch (ClientProtocolException e) {
            logger.error("【createWorkFlow接口】客户端连接异常：" + e);
            result.setState("error");
            result.setMsg("【createWorkFlow接口】客户端连接异常：" + e);
        } catch (IOException e) {
            result.setState(Constant.STATE_ERROR);
            logger.error("【createWorkFlow接口】客户端IO异常：" + e);
            result.setMsg("【createWorkFlow接口】客户端连接异常：" + e);
        } catch (Exception e) {
            result.setState(Constant.STATE_ERROR);
            result.setMsg("【createWorkFlow接口】连接异常：" + e);
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    logger.error("【startJob接口】关闭response响应异常：" + e);
                }
            }
            try {
                if (httpclient != null) {
                    httpclient.close();
                }
            } catch (IOException e) {
                logger.error("【startJob接口】关闭客户端异常：" + e);
            }
        }
        return result;
    }

    @Override
    public Result startJob(String loginUser, String loginPassword, String hostName, String receivers, String jobName, String projectName) {
        Result result = new Result();
        ProcessInstanceMapper processInstanceMapper = DBManager.processInstanceMapper();
        ProcessDefinition processDefinition = processInstanceMapper.queryProcessDefinitionId(jobName, projectName);
        String processDefinitionId = processDefinition.getId().toString();
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("failureStrategy", "END"));
        parameters.add(new BasicNameValuePair("processDefinitionId", processDefinitionId));
        parameters.add(new BasicNameValuePair("processInstancePriority", "HIGH"));
        parameters.add(new BasicNameValuePair("scheduleTime", ""));
        parameters.add(new BasicNameValuePair("warningGroupId", "1"));
        parameters.add(new BasicNameValuePair("warningType", "NONE"));
        parameters.add(new BasicNameValuePair("execType", ""));
        parameters.add(new BasicNameValuePair("receivers", receivers));
        parameters.add(new BasicNameValuePair("receiversCc", ""));
        parameters.add(new BasicNameValuePair("runMode", ""));
        parameters.add(new BasicNameValuePair("startNodeList", ""));
        parameters.add(new BasicNameValuePair("taskDependType", ""));
        parameters.add(new BasicNameValuePair("timeout", ""));
        parameters.add(new BasicNameValuePair("workerGroupId", "-1"));
        HttpClient httpClient = new HttpClient(parameters, hostName + Constant.START_JOB_CONN.replace("${projectName}", projectName), getSessionId().getData(), Constant.POST);
        httpClient.submit(result);
        return result;
    }

    @Override
    public Result releaseState(LineState lineState, Result result, String hostName) {
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("processId", lineState.getProcessDefinitionId()));
        // 1为上线 ，0为下线
        parameters.add(new BasicNameValuePair("releaseState", lineState.getFlag()));
        HttpClient httpClient = new HttpClient(parameters, hostName + Constant.STATE.replace("${projectName}", lineState.getProjectName()), getSessionId().getData(), Constant.POST);
        return httpClient.submit(result);
    }

    @Override
    public Result updateProcessDefinition(ProcessDefinition processDefinition) {
        ProcessInstanceMapper processInstanceMapper = DBManager.processInstanceMapper();
        String jobName = processDefinition.getName();
        String projectName = sheetEnv.getProjectName();
        String hostName = sheetEnv.getIp() + ":" + sheetEnv.getPort();
        ProcessDefinition processDefinitionNew = processInstanceMapper.queryProcessDefinitionId(jobName, projectName);
        String processDefinitionId = processDefinitionNew.getId().toString();

        LineState lineState = new LineState();
        lineState.setFlag(Constant.OFFLINE);
        lineState.setJobName(jobName);
        lineState.setProjectName(projectName);
        lineState.setProcessDefinitionId(processDefinitionId);
        Result result = new Result();
        releaseState(lineState, result, hostName);
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("connects", processDefinition.getConnects()));
        parameters.add(new BasicNameValuePair("id", processDefinitionId));
        parameters.add(new BasicNameValuePair("locations", processDefinition.getLocations()));
        parameters.add(new BasicNameValuePair("name", jobName));
        parameters.add(new BasicNameValuePair("processDefinitionJson", processDefinition.getProcessDefinitionJson()));
        parameters.add(new BasicNameValuePair("projectName", projectName));
        parameters.add(new BasicNameValuePair("description", processDefinition.getDescription()));
        HttpClient httpClient = new HttpClient(parameters, hostName + Constant.PROCESS_UPDATE.replace("${projectName}", projectName), getSessionId().getData(), Constant.POST);
        result = httpClient.submit(result);
        lineState.setFlag(Constant.ONLINE);
        return releaseState(lineState, result, hostName);
    }

    @Override
    public Result deleteProcessDefinition(String taskName) {
        //删除任务
        ProcessInstanceMapper processInstanceMapper = DBManager.processInstanceMapper();
        ProcessDefinition processDefinitionNew = processInstanceMapper.queryProcessDefinitionId(taskName, sheetEnv.getProjectName());
        String processDefinitionId = processDefinitionNew.getId().toString();
        LineState lineState = new LineState();
        lineState.setFlag(Constant.OFFLINE);
        lineState.setJobName(taskName);
        lineState.setProjectName(sheetEnv.getProjectName());
        lineState.setProcessDefinitionId(processDefinitionId);
        String hostName = sheetEnv.getIp() + ":" + sheetEnv.getPort();
        Result result = new Result();
        releaseState(lineState, result, hostName);
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("projectName", sheetEnv.getProjectName()));
        parameters.add(new BasicNameValuePair("processDefinitionId", processDefinitionId));
        HttpClient httpClient = new HttpClient(parameters, hostName + Constant.PROCESS_DELETE.replace("${projectName}", sheetEnv.getProjectName()), getSessionId().getData(), Constant.GET);
        return httpClient.submit(result);
    }
}

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
import java.util.Map;

import static iquantex.com.dolphinscheduler.utils.JsonUtil.json2Str;
import static iquantex.com.dolphinscheduler.utils.ParamsUtil.getLocalParams;
import static iquantex.com.dolphinscheduler.utils.ParamsUtil.getVariables;


/**
 * @author mujp
 */
public class WorkFlowModelImpl implements WorkFlowModel {
    protected static final Log logger = LogFactory.getLog(WorkFlowModelImpl.class);
    private static final String MSG = "msg";
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
    public Result createWorkFlow(SheetEnv login, ProcessDefinition processDefinitionJson) {

        CloseableHttpResponse response = null;
        String content = null;
        CloseableHttpClient httpclient = null;
        Result result = new Result();
        try {
            httpclient = HttpClients.createDefault();
           String hostName = login.getIp()+":"+login.getPort();
            HttpPost httpPost = new HttpPost(Constant.URL_HEADER + hostName + Constant.WORK_FLOW.replace("${projectName}",login.getProjectName()));
            httpPost.setHeader("sessionId", getSessionId().getData());
            List<NameValuePair> parameters = new ArrayList<>();
            parameters.add(new BasicNameValuePair("connects", processDefinitionJson.getConnects()));
            parameters.add(new BasicNameValuePair("description", processDefinitionJson.getDescription()));
            parameters.add(new BasicNameValuePair("locations", processDefinitionJson.getLocations()));
            parameters.add(new BasicNameValuePair("processDefinitionJson",processDefinitionJson.getProcessDefinitionJson()));
            parameters.add(new BasicNameValuePair("projectName", login.getProjectName()));
            parameters.add(new BasicNameValuePair("name", processDefinitionJson.getName()));
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters, "UTF-8");
            httpPost.setEntity(formEntity);
            response = httpclient.execute(httpPost);
            content = EntityUtils.toString(response.getEntity(), "UTF-8");
            JSONObject createJobResult = JSONObject.parseObject(content);
            if (Constant.STATE_SUCCESS.equalsIgnoreCase(createJobResult.get(MSG).toString())) {
                ProcessInstanceMapper instanceMapper = DBManager.setUp(result);
                ProcessDefinition processDefinition = instanceMapper.queryProcessDefinitionId(processDefinitionJson.getName(), login.getProjectName());
                String processDefinitionId = processDefinition.getId().toString();
                LineState lineState = new LineState();
                lineState.setFlag("1");
                lineState.setJobName(processDefinitionJson.getName());
                lineState.setProjectName(login.getProjectName());
                lineState.setProcessDefinitionId(processDefinitionId);
                releaseState(lineState,result,hostName);
            }else {
                result.setState(Constant.STATE_ERROR);
                result.setMsg(createJobResult.get(MSG).toString());
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
                if (httpclient !=null) {
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
        ProcessInstanceMapper processInstanceMapper = DBManager.setUp(result);
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
        HttpClient httpClient = new HttpClient(parameters, hostName + Constant.START_JOB_CONN.replace("${projectName}",projectName), getSessionId().getData(), Constant.POST);
        httpClient.submit(result);
        return result;
    }

    @Override
    public void releaseState(LineState lineState, Result result, String hostName) {
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("processId", lineState.getProcessDefinitionId()));
        // 1为上线 ，0为下线
        parameters.add(new BasicNameValuePair("releaseState", lineState.getFlag()));
        HttpClient httpClient = new HttpClient(parameters, hostName + Constant.STATE.replace("${projectName}",lineState.getProjectName()), getSessionId().getData(), Constant.POST);
        httpClient.submit(result);
    }

    @Override
    public void updateProcessDefinition(List<LocalParams> localParamsUpdate, String jobName, String projectName, String loginUser, String loginPassword, String hostName,Result result) {
        ProcessInstanceMapper processInstanceMapper = DBManager.setUp(result);
        ProcessDefinition processDefinition = processInstanceMapper.queryProcessDefinitionId(jobName, projectName);
        String processDefinitionId = processDefinition.getId().toString();
        String processDefinitionJson = processDefinition.getProcessDefinitionJson();
        Map<String, String> updateMap = json2Str(processDefinitionJson);
        String localParams = updateMap.get("localParams");
        String getLocalParams = getLocalParams(localParamsUpdate);
        String rawScript = updateMap.get("rawScript");
        String variables = getVariables(localParamsUpdate);
        processDefinitionJson = processDefinitionJson.replace(rawScript, variables).replace(localParams, getLocalParams);
        String locations = processDefinition.getLocations();
        LineState lineState = new LineState();
        lineState.setFlag("0");
        lineState.setJobName(jobName);
        lineState.setProjectName(projectName);
        lineState.setProcessDefinitionId(processDefinitionId);
        releaseState(lineState,result,hostName);
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("connects", "[]"));
        parameters.add(new BasicNameValuePair("id", processDefinitionId));
        parameters.add(new BasicNameValuePair("locations", locations));
        parameters.add(new BasicNameValuePair("name", jobName));
        parameters.add(new BasicNameValuePair("processDefinitionJson", processDefinitionJson));
        parameters.add(new BasicNameValuePair("projectName", projectName));
        parameters.add(new BasicNameValuePair("description", ""));
        HttpClient httpClient = new HttpClient(parameters, hostName + Constant.PROCESS_UPDATE.replace("${projectName}",projectName), getSessionId().getData(), Constant.POST);
        httpClient.submit(result);
        lineState.setFlag("1");
        releaseState(lineState,result,hostName);
    }

}

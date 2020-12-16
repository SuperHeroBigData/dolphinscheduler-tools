package iquantex.com.dolphinscheduler.mapper.impl;


import iquantex.com.dolphinscheduler.pojo.ProcessDefinition;
import iquantex.com.dolphinscheduler.pojo.Result;
import iquantex.com.dolphinscheduler.mapper.ProcessInstanceMapper;
import iquantex.com.dolphinscheduler.utils.DBManager;

/**
 * @author mujp
 */
public class ProcessInstanceMapperImpl implements ProcessInstanceMapper {

    private final ProcessInstanceMapper processInstanceMapper;

    public ProcessInstanceMapperImpl() {
        this.processInstanceMapper = DBManager.setUp(new Result());
    }

    @Override
    public String queryTaskInstIdByJobName(String jobName, String projectName) {
        return processInstanceMapper.queryTaskInstIdByJobName(jobName,projectName);
    }

    @Override
    public ProcessDefinition queryProcessDefinitionId(String jobName, String projectName) {
        return processInstanceMapper.queryProcessDefinitionId(jobName,projectName);
    }

    @Override
    public String queryProcessDefinitionJson(String jobName, String projectName) {
        return processInstanceMapper.queryProcessDefinitionJson(jobName,projectName);
    }

    @Override
    public long queryDataSourceId(String sourceName) {
        return processInstanceMapper.queryDataSourceId(sourceName);
    }

    @Override
    public long queryTenantId(String tenantName) {
        return processInstanceMapper.queryTenantId(tenantName);
    }

    @Override
    public long queryProjectId(String name) {
        return processInstanceMapper.queryProjectId(name);
    }

    @Override
    public long getSchedulerId(String processDefinitionId) {
        return processInstanceMapper.getSchedulerId(processDefinitionId);
    }
}

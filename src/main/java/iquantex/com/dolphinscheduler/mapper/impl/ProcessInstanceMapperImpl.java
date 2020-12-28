package iquantex.com.dolphinscheduler.mapper.impl;


import iquantex.com.dolphinscheduler.mapper.ProcessInstanceMapper;
import iquantex.com.dolphinscheduler.pojo.ProcessDefinition;
import iquantex.com.dolphinscheduler.utils.DBManager;

import java.util.List;

/**
 * @author mujp
 */
public class ProcessInstanceMapperImpl implements ProcessInstanceMapper {

    private final ProcessInstanceMapper processInstanceMapper;

    public ProcessInstanceMapperImpl() {
        this.processInstanceMapper = DBManager.processInstanceMapper();
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
    public List<String> batchQueryProcessDefinitionId(List<String> processName, String projectName) {
        return processInstanceMapper.batchQueryProcessDefinitionId(processName,projectName);
    }

}

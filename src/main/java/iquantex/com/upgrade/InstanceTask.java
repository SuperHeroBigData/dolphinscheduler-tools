package iquantex.com.upgrade;

import iquantex.com.dolphinscheduler.pojo.ProcessDefinition;
import iquantex.com.dolphinscheduler.mapper.ProcessInstanceMapper;
import iquantex.com.dolphinscheduler.mapper.impl.ProcessInstanceMapperImpl;

import java.util.Objects;

import static iquantex.com.utils.ParamUtils.getInstanceEnv;

/**
 * @ClassName task
 * @Description TODO 获取依赖任务Id
 * @Author jianping.mu
 * @Date 2020/11/26 11:23 上午
 * @Version 1.0
 */
public class InstanceTask {
    private final ProcessInstanceMapper processInstanceMapper;

    public InstanceTask() {
        this.processInstanceMapper = new ProcessInstanceMapperImpl();
    }

    /**
     * 获取依赖作业Id
     *
     * @param jobName
     * @param projectName
     * @return
     */
    public ProcessDefinition getProcessDefinitionId(String jobName, String projectName) {
        if (Objects.isNull(jobName) && Objects.isNull(projectName)) {
            throw new RuntimeException("excel任务配置 jobName或 projectName 为空.");
        }
        return processInstanceMapper
                .queryProcessDefinitionId(jobName, projectName);
    }


    /**
     * 获取数据源Id
     *
     * @param sourceName
     * @return
     */
    public long getDataSourceId(String sourceName) {
        if (Objects.isNull(sourceName)) {
            throw new RuntimeException("excel任务配置 sourceName 为空.");
        }
        return processInstanceMapper.queryDataSourceId(sourceName);
    }

    /**
     * 获取租户Id
     *
     * @return
     */
    public long getTenantId() {
        String tenantName = getInstanceEnv().getTenant();
        if (Objects.isNull(tenantName)) {
            throw new RuntimeException("excel任务配置 tenantName 为空.");
        }
        return processInstanceMapper.queryTenantId(tenantName);
    }

}

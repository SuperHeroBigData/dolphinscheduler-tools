package iquantex.com.dolphinscheduler.mapper;

import iquantex.com.dolphinscheduler.pojo.ProcessDefinition;
import org.apache.ibatis.annotations.Param;


/**
 * @author mujp
 */
public interface ProcessInstanceMapper {
    /**
     * 获取ProcessInstanceId
     * @param jobName
     * @param projectName
     * @return
     */
    String queryTaskInstIdByJobName(@Param("jobName") String jobName,@Param("projectName") String projectName);

    /**
     * 获取taskInstId
     * @param jobName
     * @param projectName
     * @return
     */
    ProcessDefinition queryProcessDefinitionId(@Param("jobName") String jobName, @Param("projectName") String projectName);

    /**
     * 获取Job参数
     * @param jobName
     * @param projectName
     * @return
     */
    String queryProcessDefinitionJson(@Param("jobName") String jobName,@Param("projectName") String projectName);

    /**
     * 查询数据源Id
     * @param sourceName
     * @return
     */
    long queryDataSourceId(@Param("sourceName") String sourceName);

    /**
     * 查询租户Id
     * @param tenantName
     * @return
     */
    long queryTenantId(@Param("tenantName") String tenantName);

    /**
     * 查询项目Id
     * @param name
     * @return
     */
    long queryProjectId(@Param("name") String name);


    /**
     * 查询定时任务Id
     * @param processDefinitionId
     * @return
     */
    long getSchedulerId(@Param("processDefinitionId") String processDefinitionId);

}


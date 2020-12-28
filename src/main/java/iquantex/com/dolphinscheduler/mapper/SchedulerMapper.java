package iquantex.com.dolphinscheduler.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * @ClassName SchdedulerMapper
 * @Description TODO
 * @Author jianping.mu
 * @Date 2020/12/18 3:02 下午
 * @Version 1.0
 */
public interface SchedulerMapper {
    /**
     * 查询定时任务Id
     * @param processDefinitionId
     * @return
     */
    long getSchedulerId(@Param("processDefinitionId") String processDefinitionId);

}

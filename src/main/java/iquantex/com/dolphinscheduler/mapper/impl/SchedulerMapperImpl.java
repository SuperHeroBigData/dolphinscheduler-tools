package iquantex.com.dolphinscheduler.mapper.impl;

import iquantex.com.dolphinscheduler.mapper.SchedulerMapper;
import iquantex.com.dolphinscheduler.utils.DBManager;

/**
 * @ClassName SchedulerMapperimpl
 * @Description TODO
 * @Author jianping.mu
 * @Date 2020/12/15 2:59 下午
 * @Version 1.0
 */
public class SchedulerMapperImpl implements SchedulerMapper {
    private final SchedulerMapper schedulerMapper;

    public SchedulerMapperImpl() {
        this.schedulerMapper = DBManager.schedulerMapper();
    }

    @Override
    public long getSchedulerId(String processDefinitionId) {
        return schedulerMapper.getSchedulerId(processDefinitionId);
    }
}

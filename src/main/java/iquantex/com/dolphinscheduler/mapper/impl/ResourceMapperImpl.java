package iquantex.com.dolphinscheduler.mapper.impl;

import iquantex.com.dolphinscheduler.mapper.ResourceMapper;
import iquantex.com.dolphinscheduler.mapper.SchedulerMapper;
import iquantex.com.dolphinscheduler.utils.DBManager;

import java.util.List;

import static iquantex.com.dolphinscheduler.utils.DBManager.schedulerMapper;

/**
 * @ClassName ResourceMapperImpl
 * @Description TODO
 * @Author franky
 * @Date 2021/08/01 2:59 下午
 * @Version 1.0
 */

public class ResourceMapperImpl implements ResourceMapper {

    private final ResourceMapper resourceMapper;

    public ResourceMapperImpl() {
        this.resourceMapper = DBManager.resourceMapper;
    }

    @Override
    public List<String> getResourceList(String fullName) {
        List<String> resourceList = resourceMapper.getResourceList(fullName);
        return resourceList;
    }
}

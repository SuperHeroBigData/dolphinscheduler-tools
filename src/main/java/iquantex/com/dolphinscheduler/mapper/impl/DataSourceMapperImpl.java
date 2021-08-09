package iquantex.com.dolphinscheduler.mapper.impl;

import iquantex.com.dolphinscheduler.mapper.DataSourceMapper;

/**
 * DataSourceImpl实现类
 *
 * @author franky
 * @date 2021-08-03 14:18
 **/
public class DataSourceMapperImpl implements DataSourceMapper {
    private DataSourceMapper dataSourceMapper;
    @Override
    public Integer getDatasourceIdByName(String name) {
        return dataSourceMapper.getDatasourceIdByName(name);
    }
}
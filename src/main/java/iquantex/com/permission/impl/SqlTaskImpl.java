package iquantex.com.permission.impl;

import iquantex.com.dolphinscheduler.mapper.DataSourceMapper;
import iquantex.com.dolphinscheduler.mapper.ResourceMapper;
import iquantex.com.dolphinscheduler.mapper.impl.DataSourceMapperImpl;
import iquantex.com.dolphinscheduler.mapper.impl.ResourceMapperImpl;
import iquantex.com.dolphinscheduler.pojo.LocalParams;
import iquantex.com.easyexcel.SheetParam;
import iquantex.com.entity.ParentTask;
import iquantex.com.entity.shell.ShellParameters;
import iquantex.com.entity.sql.SqlParameters;
import iquantex.com.entity.taskConfig.ShellConfig;
import iquantex.com.entity.taskConfig.SqlCommon;
import iquantex.com.entity.taskConfig.SqlConfig;
import iquantex.com.process.Property;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Sql任务配置实现
 *
 * @author franky
 * @date 2021-08-03 9:45
 **/
public class SqlTaskImpl extends AbstractTask {
    private final SheetParam sheet;
    private SqlParameters sqlParameters;
    private final boolean flag;
    private final DataSourceMapper dataSourceMapper;
    private ParentTask parentTask;
    public SqlTaskImpl(SheetParam sheet, List<Property> localParamsList, ParentTask parentTask, boolean flag) {
        super(sheet,parentTask);
        this.sqlParameters = (SqlParameters) parentTask.getAbstractParameters();
        this.sqlParameters.setLocalParams(localParamsList);
        this.dataSourceMapper=new DataSourceMapperImpl();
        this.sheet = sheet;
        this.flag = flag;
    }

    /**
     * 封装Sql类型参数
     *
     * @return
     */
    @Override
    public ParentTask convertToData() {
        parentTask= super.convertToData();
        SqlConfig sqlConfig = (SqlConfig) sheet.getTaskConfig();
        SqlCommon sqlCommon = sqlConfig.getSqlCommon();
        sqlParameters.setConnParams(sqlCommon.getConnParams());
        sqlParameters.setType(sqlCommon.getType());
        Integer datasourceId = dataSourceMapper.getDatasourceIdByName(sqlCommon.getData_source());
        sqlParameters.setDatasource(datasourceId);
        sqlParameters.setSql(sqlConfig.getSql());
        sqlParameters.setUdfs(sqlCommon.getUdfs());
        sqlParameters.setSqlType(Integer.parseInt(sqlCommon.getSqlType()));
        if(sqlCommon.isSendEmail())
        {
            sqlParameters.setTitle(sqlCommon.getTitle());
            sqlParameters.setReceivers(sqlCommon.getReceivers());
            sqlParameters.setReceiversCc(sqlCommon.getReceiversCc());
            sqlParameters.setShowType(sqlCommon.getShowType());
        }
        sqlParameters.setPreStatements(sqlConfig.getPreStatements());
        sqlParameters.setPostStatements(sqlConfig.getPostStatements());
        parentTask.setAbstractParameters(sqlParameters);
        return parentTask;
    }

}
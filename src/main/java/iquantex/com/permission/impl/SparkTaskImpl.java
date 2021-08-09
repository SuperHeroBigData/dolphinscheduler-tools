package iquantex.com.permission.impl;

import iquantex.com.dolphinscheduler.mapper.DataSourceMapper;
import iquantex.com.dolphinscheduler.mapper.ResourceMapper;
import iquantex.com.dolphinscheduler.mapper.impl.DataSourceMapperImpl;
import iquantex.com.dolphinscheduler.mapper.impl.ResourceMapperImpl;
import iquantex.com.easyexcel.SheetParam;
import iquantex.com.entity.ParentTask;
import iquantex.com.entity.spark.SparkParameters;
import iquantex.com.entity.sql.SqlParameters;
import iquantex.com.entity.taskConfig.SparkCommon;
import iquantex.com.entity.taskConfig.SparkConfig;
import iquantex.com.entity.taskConfig.SqlCommon;
import iquantex.com.entity.taskConfig.SqlConfig;
import iquantex.com.process.Property;

import java.util.List;

/**
 * Sql任务配置实现
 *
 * @author franky
 * @date 2021-08-03 9:45
 **/
public class SparkTaskImpl extends AbstractTask {
    private final SheetParam sheet;
    private SparkParameters sparkParameters;
    private final boolean flag;
    private ParentTask parentTask;
    private ResourceMapper resourceMapper;
    public SparkTaskImpl(SheetParam sheet, List<Property> localParamsList, ParentTask parentTask, boolean flag) {
        super(sheet,parentTask);
        this.sparkParameters = (SparkParameters) parentTask.getAbstractParameters();
        this.sparkParameters.setLocalParams(localParamsList);
        this.resourceMapper=new ResourceMapperImpl();
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
        SparkConfig sparkConfig = (SparkConfig) sheet.getTaskConfig();
        SparkCommon sparkCommon = sparkConfig.getSparkCommon();
        //submit_url com.necibook.Wordcount,test/wordcount.jar,3 2 3
        String[] sparkArgs = sparkConfig.getSubmit_url().split(",");
        if(sparkArgs.length>2)
        {
            String[] args = sparkArgs[2].split(" ");
            sparkParameters.setMainArgs();
        }
        sparkParameters.setMainJar();
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
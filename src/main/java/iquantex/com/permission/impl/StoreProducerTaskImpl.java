package iquantex.com.permission.impl;

import com.alibaba.fastjson.JSONArray;
import iquantex.com.entity.LocalParams;
import iquantex.com.easyexcel.SheetParam;
import iquantex.com.entity.stroedprodure.StoredProcedureParameters;
import iquantex.com.entity.stroedprodure.StoredProcedureParams;
import iquantex.com.enums.DatabaseType;
import iquantex.com.enums.TaskType;
import iquantex.com.upgrade.InstanceTask;
import iquantex.com.utils.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName StoreProducerTaskImpl
 * @Description TODO
 * @Author jianping.mu
 * @Date 2020/11/17 4:28 下午
 * @Version 1.0
 */
public class StoreProducerTaskImpl extends AbstractTask {
    private final SheetParam sheet;
    private StoredProcedureParameters storedProcedureParameters;
    private final StoredProcedureParams params;
    private final boolean flag;

    public StoreProducerTaskImpl(SheetParam sheet, List<LocalParams> localParamsList,StoredProcedureParameters storedProcedureParameters,boolean flag) {
        super(sheet,storedProcedureParameters);
        this.sheet = sheet;
        this.storedProcedureParameters = storedProcedureParameters;
        this.params = new StoredProcedureParams();
        this.params.setLocalParams(localParamsList);
        this.flag = flag;
    }

    /**
     * 封装存储过程参数
     * @return
     */
    @Override
    public StoredProcedureParameters convertToData() {
        storedProcedureParameters =(StoredProcedureParameters) super.convertToData();
        storedProcedureParameters.setDependence(new JSONArray());
        storedProcedureParameters.setType(TaskType.PROCEDURE.name());
        storedProcedureParameters.setMaxRetryTimes(Constant.MAX_RETRY_TIMES);
        String taskName = sheet.getSubApplication() + "." + sheet.getTableName();
        storedProcedureParameters.setName(taskName);

        if (flag){
            List<String> preTask = super.getPreTask(taskName);
            storedProcedureParameters.setPreTasks(preTask);
        }else {
            storedProcedureParameters.setPreTasks(new ArrayList<>());
        }

        params.setDatasource(new InstanceTask().getDataSourceId(sheet.getDatasourceName()));
        String method = sheet.getSubApplication()+"."+sheet.getPkgName();
        params.setMethod(method);
        params.setType(DatabaseType.valueOf(sheet.getDatasourceType()));
        params.setLocalParams(new ArrayList<>());
        storedProcedureParameters.setParams(params);

        return storedProcedureParameters;
    }
}

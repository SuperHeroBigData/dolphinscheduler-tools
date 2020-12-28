package iquantex.com.permission.impl;

import com.alibaba.fastjson.JSONObject;
import iquantex.com.dolphinscheduler.api.exceptions.TasksException;
import iquantex.com.dolphinscheduler.pojo.ProcessDefinition;
import iquantex.com.entity.SheetParam;
import iquantex.com.entity.dependent.DependItemList;
import iquantex.com.entity.dependent.DependParameters;
import iquantex.com.entity.dependent.DependTaskList;
import iquantex.com.entity.dependent.Dependence;
import iquantex.com.enums.TaskType;
import iquantex.com.upgrade.InstanceTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName DenpendtTaskImpl
 * @Description TODO
 * @Author jianping.mu
 * @Date 2020/11/30 3:57 下午
 * @Version 1.0
 */
public class DependentTaskImpl extends AbstractTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(DependentTaskImpl.class);

    private final SheetParam sheet;
    private DependParameters dependParameters;
    private final boolean flag;
    public DependentTaskImpl(SheetParam sheet, DependParameters dependParameters,boolean flag) {
        super(sheet, dependParameters);
        this.dependParameters = dependParameters;
        this.sheet = sheet;
        this.flag = flag;
    }

    /**
     * 封装依赖检查参数
     * @return
     */
    @Override
    public DependParameters convertToData() {
        dependParameters = (DependParameters) super.convertToData();
        dependParameters.setParams(new JSONObject());
        dependParameters.setType(TaskType.DEPENDENT.name());
        String taskName = sheet.getSubApplication() + "." + sheet.getTableName();
        dependParameters.setName(taskName + "_" + TaskType.DEPENDENT.name());
        if (flag){
            List<String> preTask = super.getPreTask(taskName);
            dependParameters.setPreTasks(preTask);
        }else {
            dependParameters.setPreTasks(new ArrayList<>());
        }

        InstanceTask instanceTask = new InstanceTask();
        Dependence dependence = new Dependence();
        dependenceParamPackage(dependence, instanceTask);

        dependParameters.setDependence(dependence);

        return dependParameters;
    }

    /**
     * 依赖格式转换
     *
     * @param sheet
     * @param dependItemListList
     */
    public void taskDependentJoint(SheetParam sheet, List<DependItemList> dependItemListList, InstanceTask instanceTask) {
        LOGGER.info("【taskDependentJoint】依赖格式转换");
        DependItemList dependItemList = null;
        String dependParam = sheet.getDepend();
        for (String dependent :
                dependParam.replaceAll("\n+","\n").split("\n")) {
            dependItemList = new DependItemList();

            dependent = dependent.replace("_x000D_","");
            ProcessDefinition processDefinition = instanceTask.getProcessDefinitionId(dependent, sheet.getApplication());
            if (Objects.isNull(processDefinition)) {
                LOGGER.error(dependent + " 元数据库中依赖不存在");
                throw new TasksException(dependent + " 元数据库中依赖不存在");
            }
            dependItemList.setProjectId(processDefinition.getProjectId());
            dependItemList.setDefinitionId(processDefinition.getId());
            //TODO 检查依赖ALL改为具体任务

            dependItemList.setDepTasks(dependent);
            dependItemListList.add(dependItemList);
        }

    }

    /**
     * 封装依赖参数
     *
     * @param dependence
     * @param instanceTask
     */
    public void dependenceParamPackage(Dependence dependence, InstanceTask instanceTask) {
        DependItemList dependItem = new DependItemList();
        dependItem.setDefinitionId(instanceTask.getDataSourceId(sheet.getDatasourceName()));

        List<DependItemList> dependItemList = new ArrayList<>();
        taskDependentJoint(sheet, dependItemList, instanceTask);


        DependTaskList dependTask = new DependTaskList();
        dependTask.setDependItemList(dependItemList);

        List<DependTaskList> dependTaskList = new ArrayList<>();
        dependTaskList.add(dependTask);
        dependence.setDependTaskList(dependTaskList);
    }

}

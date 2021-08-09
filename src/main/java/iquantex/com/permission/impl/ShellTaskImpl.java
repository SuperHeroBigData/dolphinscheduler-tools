package iquantex.com.permission.impl;

import iquantex.com.dolphinscheduler.mapper.ResourceMapper;
import iquantex.com.dolphinscheduler.mapper.impl.ResourceMapperImpl;
import iquantex.com.easyexcel.SheetParam;
import iquantex.com.entity.ParentTask;
import iquantex.com.entity.dsentity.Resource;
import iquantex.com.entity.shell.ShellParameters;
import iquantex.com.entity.taskConfig.ShellConfig;
import iquantex.com.process.Property;
import iquantex.com.process.ResourceInfo;
import org.springframework.context.annotation.ImportResource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @ClassName ShellTaskImpl
 * @Description TODO
 * @Author jianping.mu
 * @Date 2020/11/17 4:27 下午
 * @Version 1.0
 */
public class ShellTaskImpl extends AbstractTask {
    private final SheetParam sheet;
    private ShellParameters shellParameters;
    private final boolean flag;
    @javax.annotation.Resource
    private  ResourceMapper resourceMapper;
    private ParentTask parentTask;
    public ShellTaskImpl(SheetParam sheet, List<Property> localParamsList, ParentTask parentTask, boolean flag) {
        super(sheet,parentTask);
//        this.resourceMapper=new ResourceMapperImpl();
        this.shellParameters = (ShellParameters) parentTask.getAbstractParameters();

        this.shellParameters.setLocalParams(localParamsList);
        this.sheet = sheet;
        this.flag = flag;
    }

    /**
     * 封装shell类型参数
     *
     * @return
     */
    @Override
    public ParentTask convertToData() {
        parentTask= super.convertToData();
        String[] resource = sheet.getResourceList().split(",");
        HashSet<ResourceInfo> resourcesSet = new HashSet<>();
        for (int i = 0; i < resource.length; i++) {
            List<ResourceInfo> resourceList = resourceMapper.queryResourceByfullName(resource[i]);
            resourcesSet.addAll(resourceList);
        }
        ArrayList<ResourceInfo> totalResource = new ArrayList<>(resourcesSet);
        shellParameters.setResourceList(totalResource);
        ShellConfig shellConfig = (ShellConfig) sheet.getTaskConfig();
        shellParameters.setRawScript(shellConfig.getRawScript());
        parentTask.setAbstractParameters(shellParameters);
        return parentTask;
    }

}

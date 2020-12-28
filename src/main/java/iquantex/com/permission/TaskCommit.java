package iquantex.com.permission;

import iquantex.com.dolphinscheduler.pojo.ProcessDefinition;
import iquantex.com.dolphinscheduler.pojo.Result;

/**
 * @ClassName TaskCommit
 * @Description TODO
 * @Author jianping.mu
 * @Date 2020/11/17 4:25 下午
 * @Version 1.0
 */
public interface TaskCommit {
    /**
     * 获取task参数
     * @param processDefinition
     * @return
     */
    void getTaskParam(ProcessDefinition processDefinition);

}

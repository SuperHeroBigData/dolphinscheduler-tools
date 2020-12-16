package iquantex.com.entity.shell;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import iquantex.com.entity.Parameters;
import iquantex.com.entity.TimeOut;
import iquantex.com.enums.Priority;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName ShellParameters
 * @Description TODO Shell 任务参数封装
 * @Author jianping.mu
 * @Date 2020/11/13 6:17 下午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ShellParameters extends Parameters {
    /**
     * 任务依赖
     */
    private JSONObject dependence;
    /**
     * 最大重试次数
     */
    private int maxRetryTimes = 3;
    /**
     * 重试间隔
     */
    private String retryInterval = "3";

    /**
     * 超时控制
     */
    private TimeOut timeout;
    /**
     * 优先级任务
     */
    private Priority taskInstancePriority = Priority.MEDIUM;
    /**
     * 分组
     */
    private String workerGroup = "default";
    /**
     * 自定义参数
     */
    @JSONField
    private ShellParams params;
}

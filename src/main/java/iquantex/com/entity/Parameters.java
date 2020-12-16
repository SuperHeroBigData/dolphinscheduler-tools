package iquantex.com.entity;

import iquantex.com.enums.Priority;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @ClassName Parameters
 * @Description TODO
 * @Author jianping.mu
 * @Date 2020/11/25 9:39 下午
 * @Version 1.0
 */
@Data
public class Parameters {

    /**
     * 任务编码
     */
    private String id;
    /**
     * 类型	SHELL
     */
    private String type;
    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description="";
    /**
     * 运行标识
     */
    private String runFlag = "NORMAL";
    /**
     * 条件分支
     * successNode	Array	成功跳转节点
     * failedNode	Array	失败跳转节点
     */
    private final HashMap<String,Object> conditionResult = new HashMap<String,Object>(){{
        put("successNode",new ArrayList<String>(){{add("");}});
        put("failedNode",new ArrayList<String>(){{add("");}});
    }};

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
     * 前置任务
     */
    private List<String> preTasks;
}

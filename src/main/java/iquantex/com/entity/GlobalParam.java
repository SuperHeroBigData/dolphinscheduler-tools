package iquantex.com.entity;

import java.util.ArrayList;

/**
 * @ClassName globalParam
 * @Description TODO
 * @Author jianping.mu
 * @Date 2020/11/17 1:49 下午
 * @Version 1.0
 */
public class GlobalParam {
    /**
     * 全局参数
     */
   private ArrayList<String> globalParams=new ArrayList<>();
    /**
     * 流程中的任务集合
     */
   private ArrayList<String> tasks = new ArrayList<>();
    /**
     * 租户id
     */
   private int tenantId;
    /**
     * 超时时间
     */
   private int timeout;

    public ArrayList<String> getGlobalParams() {
        return globalParams;
    }

    public void setGlobalParams(ArrayList<String> globalParams) {
        this.globalParams = globalParams;
    }

    public ArrayList<String> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<String> tasks) {
        this.tasks = tasks;
    }

    public int getTenantId() {
        return tenantId;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public String toString() {
        return "GlobalParam{" +
                "globalParams=" + globalParams +
                ", tasks=" + tasks +
                ", tenantId=" + tenantId +
                ", timeout=" + timeout +
                '}';
    }

}


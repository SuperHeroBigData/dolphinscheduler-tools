package iquantex.com.entity;

import lombok.Data;

/**
 * @ClassName TimeOut
 * @Description TODO
 * @Author jianping.mu
 * @Date 2020/11/25 8:37 下午
 * @Version 1.0
 */
@Data
public class TimeOut {
    /**
     * 告警策略
     */
    private String strategy = "WARN";
    /**
     * 超时时间
     */
    private Long interval;

    private boolean enable = true;
}

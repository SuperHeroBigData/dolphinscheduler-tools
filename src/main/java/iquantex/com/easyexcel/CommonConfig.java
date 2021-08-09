package iquantex.com.easyexcel;

import iquantex.com.entity.TimeOut;
import iquantex.com.enums.Priority;
import lombok.*;
import lombok.Data;

/**
 * 节点任务公有信息
 *
 * @author franky
 * @date 2021-08-01 16:12
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonConfig {
    @Builder.Default
    private String RunFlag="NORMAL";
    @Builder.Default
    private String maxRetryTimes="3";
    @Builder.Default
    private String retryInterval="30s";
    @Builder.Default
    private String taskInstancePriority= Priority.MEDIUM.name();
    @Builder.Default
    private String workerGroup="default";
    @Builder.Default
    private TimeOut time_out=new TimeOut();
    @Builder.Default
    private String pretasksList="";
}
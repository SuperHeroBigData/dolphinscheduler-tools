package iquantex.com.entity;

import lombok.Data;

/**
 * @ClassName ExcelDependEntity
 * @Description TODO 前置依赖信息
 * @Author jianping.mu
 * @Date 2020/11/26 4:50 下午
 * @Version 1.0
 */
@Data
public class ExcelDependEntity {

    private String processName;

    private String taskName;

    private String dependName;
}

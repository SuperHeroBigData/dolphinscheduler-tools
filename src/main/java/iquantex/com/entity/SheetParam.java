package iquantex.com.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;


/**
 * @ClassName SheetOds
 * @Description TODO
 * @Author jianping.mu
 * @Date 2020/11/16 9:53 上午
 * @Version 1.0
 */
@Data
public class SheetParam {

    @ExcelProperty(value = "应用名",index=0)
    private String application;

    @ExcelProperty(value = "子应用名",index = 1)
    private String subApplication;

    @ExcelProperty(value = "表名",index = 2)
    private String tableName;

    @ExcelProperty(value = "存储过程名",index = 3)
    private String pkgName;

    @ExcelProperty(value = "任务类型",index = 4)
    private String taskType;

    @ExcelProperty(value = "任务描述",index = 5)
    private String description;

    @ExcelProperty(value = "负责人",index = 6)
    private String createdBy;

    @ExcelProperty(value = "脚本类型",index = 7)
    private String scriptType;

    @ExcelProperty(value = "任务路径",index = 8)
    private String taskPath;

    @ExcelProperty(value = "任务参数",index = 9)
    private String taskParam;

    @ExcelProperty(value = "前置依赖任务类型",index = 10)
    private String dependType;

    @ExcelProperty(value = "前置依赖任务",index = 11)
    private String depend;

    @ExcelProperty(value = "数据源名称",index = 12)
    private String datasourceName;

    @ExcelProperty(value = "数据源类型",index = 13)
    private String datasourceType;

    @ExcelProperty(value = "执行周期",index = 14)
    private String solarCalender;

    @ExcelProperty(value = "起始时间",index = 15)
    private String taskScheduler;

    @ExcelProperty(value = "失败是否重试",index = 16)
    private String cyclic;

    @ExcelProperty(value = "重试次数",index = 17)
    private String maxRerun;

    @ExcelProperty(value = "重试间隔时间",index = 18)
    private String interval;

    @ExcelProperty(value = "超时是否告警",index = 19)
    private String timeout;

    @ExcelProperty(value = "超时告警时间",index = 20)
    private String alarmTime;

    @ExcelProperty(value = "超时描述信息",index = 21)
    private String alarmMessage;

    private int sheetNumber;
}

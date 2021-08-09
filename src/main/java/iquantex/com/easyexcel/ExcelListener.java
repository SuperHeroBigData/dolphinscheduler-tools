package iquantex.com.easyexcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.holder.ReadSheetHolder;
import iquantex.com.AnalysisApplication;
import iquantex.com.dolphinscheduler.pojo.ProcessDefinition;
import iquantex.com.permission.impl.ParamConvert;
import iquantex.com.permission.impl.SubProcessTaskImpl;
import iquantex.com.utils.Constant;
import iquantex.com.utils.Constants;
import jdk.nashorn.internal.scripts.JO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName ExcelListener
 * @Description TODO
 * @Author jianping.mu
 * @Date 2020/11/24 1:44 下午
 * @Version 1.0
 */
public class ExcelListener<T> extends AnalysisEventListener<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelListener.class);
    private static final List<SheetEnv> SHEET_ENV_LIST = new ArrayList<>();
    private static final StringBuilder JOB_NAME = new StringBuilder(10);
    private static Integer currentSheetNo=0;
    private static String currentSheetName="";
    /**
     * 读取excel头
     *
     * @param filePath 文件路径
     * @param cla      对象
     */
    public void readExcelHead(String filePath, Class<?> cla) {
        EasyExcel.read(filePath, cla, new ExcelListener<>()).sheet().doRead();
    }


    /**
     * 根据规则一行一行读取数据
     *
     * @param filePath 文件路径
     * @param cla      对象
     * @param sheetNum sheet页
     * @param headNum  文件头行
     */
    public void readData(String filePath, Class<?> cla, int sheetNum, int headNum) {
        EasyExcel.read(filePath, cla, new ExcelListener<>()).sheet(sheetNum)
                .headRowNumber(headNum).doRead();
    }

    /**
     * 读取excel表头
     *
     * @param headMap
     * @param context
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        LOGGER.info("读取excel表头信息：{}", headMap);
    }

    /**
     * 读取excel内容
     *
     * @param t       解析对象
     * @param context excel文件信息
     */
    @Override
    public void invoke(T t, AnalysisContext context) {
        Integer sheetNo = context.readSheetHolder().getReadSheet().getSheetNo();
        if(!sheetNo.equals(currentSheetNo))
        {
            //执行提交工作流指令
            ProcessDefinition processDefinition = new ProcessDefinition();
            String workFlowName=currentSheetName;
            processDefinition.setName(workFlowName);
            new SubProcessTaskImpl().getTaskParam(processDefinition);
        }
        ReadSheetHolder readSheetHolder = context.readSheetHolder();
        currentSheetName = readSheetHolder.getReadSheet().getSheetName();
        currentSheetNo=readSheetHolder.getReadSheet().getSheetNo();
        convert(t, readSheetHolder.getSheetNo());
    }


    /**
     * 解析完成后实现依赖功能
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        ReadSheetHolder readSheetHolder = context.readSheetHolder();
        String sheetName = readSheetHolder.getSheetName();
        Integer sheetNo = readSheetHolder.getSheetNo();
        if (sheetNo > Constants.JOB) {
            JOB_NAME.append(sheetName);
        }
        LOGGER.info("Sheet页名字：{}，Sheet页下标：{}",
                sheetName, sheetNo + "。读取完毕！！！");

        //TODO 按照依赖关系生成Job
        if (sheetNo >= Constants.SHEET_NO) {
            ProcessDefinition processDefinition = new ProcessDefinition();
            String jobNames = JOB_NAME.substring(0, JOB_NAME.length() - 1);
            processDefinition.setName(jobNames);
            new SubProcessTaskImpl().getTaskParam(processDefinition);
            LOGGER.info("Job名字是：{}", jobNames + ",已执行完毕！！！");
        }
    }

    /**
     * 转换不同分支
     *
     * @param data
     */
    public void convert(T data, int sheetNumber) {
        if (data instanceof SheetParam) {

            if (Objects.isNull(AnalysisApplication.sheetEnv)) {
                throw new RuntimeException("初始化环境变量sheetEnv为空。");
            }
            SheetParam sheetParam = (SheetParam) data;
            sheetParam.setSheetNumber(sheetNumber);
            new ParamConvert(sheetParam);
        }
    }

}

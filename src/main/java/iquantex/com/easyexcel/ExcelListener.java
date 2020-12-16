package iquantex.com.easyexcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.holder.ReadSheetHolder;
import iquantex.com.dolphinscheduler.pojo.ProcessDefinition;
import iquantex.com.dolphinscheduler.pojo.Result;
import iquantex.com.entity.SheetEnv;
import iquantex.com.entity.SheetParam;
import iquantex.com.permission.impl.ParamConvert;
import iquantex.com.permission.impl.SubProcessTaskImpl;
import iquantex.com.utils.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static iquantex.com.utils.HttpUtil.executeResult;

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
    private static String START_TASK = null;

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
    public void readData(String filePath, Class<?> cla, int sheetNum, int headNum, String startTask) {
        START_TASK = startTask;
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
        ReadSheetHolder readSheetHolder = context.readSheetHolder();
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
        if (sheetNo > Constant.JOB) {
            JOB_NAME.append(sheetName).append("_");
        }
        LOGGER.info("Sheet页名字：{}，Sheet页下标：{}",
                sheetName, sheetNo + "。读取完毕！！！");

        //TODO 按照依赖关系生成Job
        if (sheetNo >= Constant.SHEET_NO) {
            ProcessDefinition processDefinition = new ProcessDefinition();
            String jobNames = JOB_NAME.substring(0, JOB_NAME.length() - 1);
            processDefinition.setName(jobNames);
            Result taskParam = new SubProcessTaskImpl().getTaskParam(processDefinition);

            executeResult(taskParam);
            LOGGER.info("Job名字是：{}", jobNames + ",已执行完毕！！！");
        }
    }

    /**
     * 转换不同分支
     *
     * @param data
     */
    public void convert(T data, int sheetNumber) {
        if (data instanceof SheetEnv) {
            SHEET_ENV_LIST.add((SheetEnv) data);
            //ds password加密解密操作
            /* new SheetEnvConvert(SHEET_ENV_LIST);*/
        }
        if (data instanceof SheetParam) {

            if (SHEET_ENV_LIST.isEmpty()) {
                throw new RuntimeException("初始化环境变量sheetEnv为空。");
            }
            boolean flag = false;
            SheetParam sheetParam = (SheetParam) data;
            if (Objects.nonNull(START_TASK)) {
                flag = true;
                if (Objects.equals(sheetParam.getTableName(), START_TASK)) {
                    sheetParam.setSheetNumber(sheetNumber);
                    new ParamConvert(sheetParam);
                }
            }else {
                sheetParam.setSheetNumber(sheetNumber);
                new ParamConvert(sheetParam);
            }
        }
    }

    /**
     * 获取环境队列
     *
     * @return
     */
    public static List<SheetEnv> getSheetEnvList() {
        return SHEET_ENV_LIST;
    }


}

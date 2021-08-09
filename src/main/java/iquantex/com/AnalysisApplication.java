package iquantex.com;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.metadata.ReadSheet;
import iquantex.com.easyexcel.ExcelListener;
import iquantex.com.easyexcel.SheetEnv;
import iquantex.com.easyexcel.SheetParam;
import iquantex.com.utils.ConfigUtil;
import iquantex.com.utils.Constant;
import iquantex.com.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.krb5.Config;

import java.util.List;
import java.util.Objects;

public class AnalysisApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnalysisApplication.class);
    public static  int SHEET_INDEX = 0;
    public static final int HEAD_NUM = 2;
    public static final int ARGS_LEN = 4;
    public static final int ENV_INDEX = 0;
    public static SheetEnv sheetEnv;
    public static String JOB_DDL;

    public static void main(String[] args) throws IllegalAccessException {
        //实现excel写操作
        if (Objects.nonNull(args) && args.length < ARGS_LEN) {
            new AnalysisApplication().excelEnv(args[0],args[1],args[2]);
        } else {
            throw new IllegalArgumentException("传入参数为空!!!");
        }
    }

    /**
     * 获取当前环境参数
     *
     * @param filePath excel路径
     */
    public void excelEnv(String filePath,String ddl,String sheet_index) throws IllegalAccessException {
        LOGGER.info("获取文件路径：{}", filePath);
        JOB_DDL=ddl;
        LOGGER.info("工作流逻辑：{}",JOB_DDL);
        List<ReadSheet> readSheets = EasyExcel.read(filePath).build().excelExecutor().sheetList();
        int sheet_nums = readSheets.size();
        if(Objects.isNull(sheet_index))
        {
            sheet_index=String.valueOf(sheet_nums);
        }
        if(Objects.nonNull(sheet_index)&&sheet_nums>=Integer.parseInt(sheet_index))
        {
            sheetEnv=new SheetEnv();
            SHEET_INDEX=Integer.parseInt(sheet_index);
            packageEnv();
            excelParam(filePath);
        }else
        {
            LOGGER.error("传入sheet页参数超限，不符合要求，传入{}，总页数{}",sheet_index,sheet_nums);
            throw new IllegalAccessException("传入sheet页数大于表总页数");
        }

    }

    /**
     * 遍历依赖任务
     *
     * @param filePath excel路径
     */
    public void excelParam(String filePath) {
        ExcelListener<SheetParam> listener = new ExcelListener<>();
        for (int i = 0; i < SHEET_INDEX; i++) {
            LOGGER.info("解析excel第{}页，封装工作流",SHEET_INDEX);
            listener.readData(filePath, SheetParam.class, i, HEAD_NUM);
        }
    }

    /**
     * 封装sheetEnv环境信息
     */
    public void packageEnv()
    {
        sheetEnv.setPresent(ConfigUtil.getProperty(Constants.PRESENT_ENV));
        sheetEnv.setUserName(ConfigUtil.getProperty(Constants.DS_USER_NAME));
        sheetEnv.setPassword(ConfigUtil.getProperty(Constants.DS_USER_PASSWD));
        sheetEnv.setIp(ConfigUtil.getProperty(Constants.DS_IP));
        sheetEnv.setPort(ConfigUtil.getProperty(Constants.DS_PORT));
        sheetEnv.setTenant(ConfigUtil.getProperty(Constants.DS_TENANT));
        sheetEnv.setDbType(ConfigUtil.getProperty(Constants.DS_DB_TYPE));
        sheetEnv.setDbIp(ConfigUtil.getProperty(Constants.DS_DB_IP));
        sheetEnv.setDbPort(ConfigUtil.getProperty(Constants.DS_DB_PORT));
        sheetEnv.setDbUser(ConfigUtil.getProperty(Constants.DB_USER));
        sheetEnv.setDbPassword(ConfigUtil.getProperty(Constants.DB_PASSWD));
        sheetEnv.setDbDatabase(ConfigUtil.getProperty(Constants.DB_DATABASE));
        sheetEnv.setProjectName(ConfigUtil.getProperty(Constants.projectName));
        sheetEnv.setReceivers(ConfigUtil.getProperty(Constants.RECEIVERS));
        sheetEnv.setReceiversCc(ConfigUtil.getProperty(Constants.DS_RECEIVERSCC));
    }
}

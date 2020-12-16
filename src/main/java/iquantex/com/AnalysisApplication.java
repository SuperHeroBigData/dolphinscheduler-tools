package iquantex.com;

import iquantex.com.easyexcel.ExcelListener;
import iquantex.com.entity.SheetEnv;
import iquantex.com.entity.SheetParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName AnalysisApplication
 * @Description TODO 执行主函数入口  _x000D_
 * @Author jianping.mu
 * @Date 2020/11/26 2:43 下午
 * @Version 1.0
 */
public class AnalysisApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnalysisApplication.class);
    public static final int SHEET_INDEX = 4;
    public static final int HEAD_NUM = 2;
    public static final int ENV_INDEX = 0;

    public static void main(String[] args) {
        //实现excel写的操作
            /*if (Objects.isNull(args)){
                throw new RuntimeException("传入xlsx文件不存在");
            }
        new AnalysisApplication().excelEnv(args[0]);*/

        String path1 = "/Users/mujp/Desktop/FdsDispatchDS18张表.xlsx";
        String path2 = "/Users/mujp/Desktop/FdsDispatchDS10主题表.xlsx";
        String path3 = "/Users/mujp/Desktop/FdsDispatchDS初版.xlsx";
         new AnalysisApplication().excelEnv(path2);
        // new AnalysisApplication().excelEnv(path1);
    }

    /**
     * 获取当前环境参数
     * @param filePath excel路径
     */
    public void excelEnv(String filePath) {
        LOGGER.info("获取文件路径：{}",filePath);
        ExcelListener<SheetEnv> listener = new ExcelListener<>();
        listener.readData(filePath, SheetEnv.class, ENV_INDEX, HEAD_NUM,null);
        excelParam(filePath);
    }

    /**
     * 遍历依赖任务
     * @param filePath excel路径
     */
    public void excelParam(String filePath) {
        ExcelListener<SheetParam> listener = new ExcelListener<>();
        for (int i = 1; i < SHEET_INDEX; i++) {
            listener.readData(filePath, SheetParam.class, i, HEAD_NUM,null);
        }
    }
}

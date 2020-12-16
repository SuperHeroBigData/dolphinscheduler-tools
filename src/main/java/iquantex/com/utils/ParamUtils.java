package iquantex.com.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import iquantex.com.entity.ExcelDependEntity;
import iquantex.com.entity.LocalParams;
import iquantex.com.entity.SheetEnv;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static iquantex.com.easyexcel.ExcelListener.getSheetEnvList;

/**
 * @ClassName ParamUtils
 * @Description TODO
 * @Author jianping.mu
 * @Date 2020/11/24 5:21 下午
 * @Version 1.0
 */
public class ParamUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParamUtils.class);

    private static SheetEnv sheetEnv = null;

    /**
     * 参数格式转换
     *
     * @param taskParam key=value
     * @return json
     */
    public static List<LocalParams> taskParamToList(String taskParam) {
        List<LocalParams> listLocalParams = new ArrayList<>();
        LocalParams localParams = null;
        for (String dependent :
                taskParam.split("\n")) {
            String[] keyValue = dependent.split("=");
            localParams = new LocalParams();
            localParams.setProp(keyValue[0]);
            localParams.setValue(keyValue[1]);
            listLocalParams.add(localParams);
        }
        return listLocalParams;
    }

    /**
     * 依赖格式转换
     *
     * @param dependentParam
     * @return
     */
    public static List<ExcelDependEntity> taskDependToMap(String dependentParam, ExcelDependEntity excelDependEntity) {
        List<ExcelDependEntity> excelDependEntities = new ArrayList<>();
        for (String dependent :
                dependentParam.split("\n")) {
            String[] taskInfo = dependent.split("\\.");
            excelDependEntity.setProcessName(taskInfo[0]);
            excelDependEntity.setDependName(taskInfo[1]);
            if (Constant.NUMBER == taskInfo.length) {
                excelDependEntity.setTaskName(taskInfo[2]);
            }
            excelDependEntities.add(excelDependEntity);
        }
        return excelDependEntities;
    }

    /**
     * 解析依赖字段信息
     * @param dependentParam
     * @return
     */
    public static List<String> taskDependentToList(String dependentParam){

        return Objects.isNull(dependentParam) ? new ArrayList<>() : new ArrayList<>(Arrays.asList(dependentParam.replace("_x000D_","").replaceAll("\n+","\n").split("\n")));
    }

    /**
     * 获取当前环境
     *
     * @return
     */
    public static SheetEnv getEnvInfo() {
        List<SheetEnv> envList = getSheetEnvList();
        if (CollectionUtils.isEmpty(envList)) {
            throw new RuntimeException("环境队列为空。");
        }
        String present = null;
        for (SheetEnv sheetEnv : envList
        ) {
            if (Objects.isNull(present)) {
                present = sheetEnv.getPresent();
            }

            if (Objects.nonNull(present) &&
                    Objects.equals(present, sheetEnv.getEnvironment())) {
                return sheetEnv;
            }
        }
        return null;
    }

    /**
     * 获取当前环境
     *
     * @return
     */
    public static SheetEnv getInstanceEnv() {
        if (Objects.isNull(sheetEnv)) {
            sheetEnv = getEnvInfo();
            JSONObject json = (JSONObject) JSON.toJSON(sheetEnv);
            assert json != null;
            json.put("password","******");
            json.put("dbPassword","******");
            LOGGER.info("获取环境信息：{}", json);
        }

        return sheetEnv;
    }

}

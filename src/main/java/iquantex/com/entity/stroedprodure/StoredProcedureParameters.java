package iquantex.com.entity.stroedprodure;

import com.alibaba.fastjson.JSONArray;
import iquantex.com.entity.Parameters;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName StoredProcedureParameters
 * @Description TODO
 * @Author jianping.mu
 * @Date 2020/11/13 6:18 下午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StoredProcedureParameters extends Parameters {
    /**
     * 全局参数
     */
    private StoredProcedureParams params = new StoredProcedureParams();
    /**
     * 任务依赖
     */
    private JSONArray dependence;
}

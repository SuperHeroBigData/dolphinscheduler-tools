package iquantex.com.entity.dependent;

import com.alibaba.fastjson.JSONObject;
import iquantex.com.entity.Parameters;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName DependceParameters
 * @Description TODO
 * @Author jianping.mu
 * @Date 2020/11/13 6:18 下午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DependParameters extends Parameters {
    /**
     * 任务依赖
     */
     private Dependence dependence;
    /**
     * 自定义参数
     */
    private JSONObject params;
}

package iquantex.com.entity.subprocess;

import iquantex.com.entity.Parameters;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName SubProcessParamter
 * @Description TODO
 * @Author jianping.mu
 * @Date 2020/11/25 9:25 下午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SubProcessParameters extends Parameters {
    /**
     * 对应的参数实例
     */
   private Params params;

}

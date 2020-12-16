package iquantex.com.entity;

import iquantex.com.enums.Direct;
import lombok.Data;

/**
 * @ClassName LocalParams
 * @Description TODO
 * @Author jianping.mu
 * @Date 2020/11/27 3:45 下午
 * @Version 1.0
 */
@Data
public class LocalParams {
     private String prop;
     private Direct direct = Direct.IN;
     private String type = "VARCHAR";
     private String value;
}

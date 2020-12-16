package iquantex.com.entity.shell;

import iquantex.com.entity.LocalParams;
import lombok.Data;

import java.util.List;

/**
 * @ClassName Params
 * @Description TODO
 * @Author jianping.mu
 * @Date 2020/11/27 3:47 下午
 * @Version 1.0
 */
@Data
public class ShellParams {

    private List<String> resourceList;
    /**
     * 本地参数
     */
    private List<LocalParams> localParams;
    /**
     * shell脚本
     */
    private String rawScript;
}

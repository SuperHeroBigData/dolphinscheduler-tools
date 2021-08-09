package iquantex.com.entity.taskConfig;

import iquantex.com.entity.TaskConfig;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * excel定义shellConfig
 *
 * @author franky
 * @date 2021-08-01 10:23
 **/
@Data
public class ShellConfig extends TaskConfig {
    private String rawScript="";
}
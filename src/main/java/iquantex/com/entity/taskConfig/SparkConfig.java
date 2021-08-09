package iquantex.com.entity.taskConfig;

import iquantex.com.entity.TaskConfig;
import lombok.Data;

import java.util.ArrayList;

/**
 * excel定义shellConfig
 *
 * @author franky
 * @date 2021-08-01 10:23
 **/
@Data
public class SparkConfig extends TaskConfig {
    private SparkCommon sparkCommon=new SparkCommon();
    private String submit_url;
    private String others;
}
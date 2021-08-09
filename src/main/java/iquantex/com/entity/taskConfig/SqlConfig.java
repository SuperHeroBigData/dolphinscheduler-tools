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
public class SqlConfig extends TaskConfig {
    private SqlCommon sqlCommon=new SqlCommon();
    private String connParams;
    private String sql;
    private ArrayList<String> preStatements;
    private ArrayList<String> postStatements;
}
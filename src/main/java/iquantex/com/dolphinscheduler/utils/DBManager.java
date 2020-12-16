package iquantex.com.dolphinscheduler.utils;

import iquantex.com.dolphinscheduler.pojo.Result;
import iquantex.com.dolphinscheduler.mapper.ProcessInstanceMapper;
import iquantex.com.entity.SheetEnv;
import iquantex.com.enums.DatabaseType;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

import static iquantex.com.utils.ParamUtils.getInstanceEnv;

/**
 * @author mujp
 * 使用mybatis连接数据库
 */
public class DBManager {
    public static final String RESOURCE = "mybatis-config.xml";
    public static final String JDBC_URL="jdbc:mysql://%s:%s/%s";
    public static final String DRIVER = "com.mysql.jdbc.Driver";
    public static Properties properties;
    public static ProcessInstanceMapper processInstanceMapper = null;
    /**
     * 获取数据库连接
     * @param result
     * @return
     */
    public static ProcessInstanceMapper setUp(Result result) {
        if (Objects.nonNull(processInstanceMapper)){
            return processInstanceMapper;
        }
        InputStream is = null;
        SqlSession sqlSession = null;
        try {
            Properties processInstanceMapper = getProcessInstanceMapper(getInstanceEnv());
            is = Resources.getResourceAsStream(RESOURCE);
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is,processInstanceMapper);
            sqlSession = sqlSessionFactory.openSession(true);
            DBManager.processInstanceMapper = sqlSession.getMapper(ProcessInstanceMapper.class);
        } catch (IOException e) {
            e.printStackTrace();
            result.setMsg("mybatis 连接异常。 " + result);
        }
        return processInstanceMapper;
    }

    /**
     * 配置数据库连接信息
     * @param sheetEnv
     * @return
     */
    public static Properties getProcessInstanceMapper(SheetEnv sheetEnv) {
        if (!Objects.equals(DatabaseType.MYSQL,DatabaseType.valueOf(sheetEnv.getDbType()))) {
            throw new RuntimeException("ds元数据连接仅支持MYSQL");
        }
        Properties properties = new Properties();
        properties.setProperty("jdbc.driver", DRIVER);
        String url = String.format(JDBC_URL, sheetEnv.getDbIp(), sheetEnv.getDbPort(), sheetEnv.getDbDatabase());
        properties.setProperty("jdbc.url", url);
        properties.setProperty("jdbc.username", sheetEnv.getDbUser());
        properties.setProperty("jdbc.password", sheetEnv.getDbPassword());
       return properties;
    }
}
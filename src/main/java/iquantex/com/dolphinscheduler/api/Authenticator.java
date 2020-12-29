package iquantex.com.dolphinscheduler.api;

import iquantex.com.dolphinscheduler.pojo.Result;
import iquantex.com.easyexcel.SheetEnv;

/**
 * @author mujp
 */
public interface Authenticator {
    /**
     * 用户认证获取session
     * @param login
     * @return
     */
    Result authenticate(SheetEnv login);

}

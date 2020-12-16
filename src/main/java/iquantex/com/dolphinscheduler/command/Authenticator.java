package iquantex.com.dolphinscheduler.command;

import iquantex.com.dolphinscheduler.pojo.Result;
import iquantex.com.entity.SheetEnv;

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

package web.query;

import cart.untils.ParamUtil;
import lombok.Data;

/**
 * @Author: 老张
 * @Date: 2020/3/28
 */
@Data
public class UserLoginQuery extends AbstractQuery {
    /**
     * 用户手机号码
     */
    private String phoneNumber;

    /**
     * 用户密码
     */
    private String password;

    @Override
    public void checkParam() {
        ParamUtil.isBlank(phoneNumber, "用户手机号不能为空!");
        ParamUtil.isBlank(password, "用户密码不能为空!");
    }
}

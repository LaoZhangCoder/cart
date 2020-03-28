package web.query;

import lombok.Data;

/**
 * @Author: 老张
 * @Date: 2020/3/28
 */
@Data
public class UserLoginQuery {
    /**
     * 用户手机号码
     */
    private String phoneNumber;

    /**
     * 用户密码
     */
    private String password;
}

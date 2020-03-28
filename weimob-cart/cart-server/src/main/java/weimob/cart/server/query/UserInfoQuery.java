package weimob.cart.server.query;

import lombok.Data;

/**
 * @Author: 老张
 * @Date: 2020/3/28
 */
@Data
public class UserInfoQuery {
    /**
     * 电话号码
     */
    private String phoneNumber;
    /**
     * 密码
     */
    private String password;
}

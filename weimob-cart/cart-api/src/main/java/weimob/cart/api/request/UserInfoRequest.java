package weimob.cart.api.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: 老张
 * @Date: 2020/3/28
 */
@Data
public class UserInfoRequest implements Serializable {
    private static final long serialVersionUID = -3641623513101711265L;
    /**
     * 电话号码
     */
    private String phoneNumber;
    /**
     * 密码
     */
    private String password;
}


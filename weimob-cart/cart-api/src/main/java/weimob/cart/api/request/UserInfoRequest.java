package weimob.cart.api.request;

import cart.request.AbstractRequest;
import cart.untils.ParamUtil;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: 老张
 * @Date: 2020/3/28
 */
@Data
public class UserInfoRequest extends AbstractRequest implements Serializable {
    private static final long serialVersionUID = -3641623513101711265L;
    /**
     * 电话号码
     */
    private String phoneNumber;
    /**
     * 密码
     */
    private String password;

    @Override
    public void checkParam() {
        ParamUtil.isBlank(this.password,"密码不能为空!");
        ParamUtil.isBlank(this.phoneNumber,"电话号码不能为空!");
    }
}


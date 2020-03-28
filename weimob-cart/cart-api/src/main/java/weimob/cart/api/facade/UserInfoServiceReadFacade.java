package weimob.cart.api.facade;

import cart.response.Response;
import weimob.cart.api.request.UserInfoRequest;
import weimob.cart.api.response.UserInfo;

/**
 * @Author: 老张
 * @Date: 2020/3/28
 */
public interface UserInfoServiceReadFacade {
    /**
     *
     * @param request
     * @return
     * @descition 获取用户信息
     */
    Response<UserInfo> getUserInfo(UserInfoRequest request);
}

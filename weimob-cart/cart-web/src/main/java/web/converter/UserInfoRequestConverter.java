package web.converter;

import web.query.UserLoginQuery;
import weimob.cart.api.request.UserInfoRequest;

/**
 * @Author: 老张
 * @Date: 2020/4/10
 * @Description 用户信息请求转换器
 */
public class UserInfoRequestConverter {
    public static UserInfoRequest getUserInfoRequest(UserLoginQuery query) {
        UserInfoRequest userInfoRequest = new UserInfoRequest();
        userInfoRequest.setPassword(query.getPassword());
        userInfoRequest.setPhoneNumber(query.getPhoneNumber());
        return userInfoRequest;
    }
}

package weimob.cart.server.converter;

import weimob.cart.api.request.UserInfoRequest;
import weimob.cart.server.query.UserInfoQuery;

/**
 * @Author: 老张
 * @Date: 2020/4/11
 */
public class UserInfoQueryConverter {
    public static UserInfoQuery userInfoQuery(UserInfoRequest request) {
        UserInfoQuery userInfoQuery = new UserInfoQuery();
        userInfoQuery.setPassword(request.getPassword());
        userInfoQuery.setPhoneNumber(request.getPhoneNumber());
        return userInfoQuery;
    }
}

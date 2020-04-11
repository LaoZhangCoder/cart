package weimob.cart.server.converter;

import cart.response.Response;
import org.springframework.beans.BeanUtils;
import weimob.cart.api.response.UserInfo;
import weimob.cart.server.domain.dto.UserInfoDto;

/**
 * @Author: 老张
 * @Date: 2020/4/11
 */
public class UserInfoResponseConverter {

    public static Response<UserInfo> getUserInfoResponse(UserInfoDto userInfoDto) {
        Response<UserInfo> userInfoResponse = new Response<>();
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userInfoDto, userInfo);
        userInfoResponse.setResult(userInfo);
        return userInfoResponse;
    }
}

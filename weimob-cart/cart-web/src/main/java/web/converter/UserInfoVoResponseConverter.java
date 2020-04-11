package web.converter;

import cart.response.Response;
import web.response.UserInfoVo;
import weimob.cart.api.response.UserInfo;

/**
 * @Author: 老张
 * @Date: 2020/4/11
 */
public class UserInfoVoResponseConverter {
    public static Response<UserInfoVo> getUserInfoVoResponse(Response<UserInfo> userInfo, Response<UserInfoVo> userInfoVoResponse) {
        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setUserId(userInfo.getResult().getUserId());
        userInfoVo.setUserName(userInfo.getResult().getUserName());
        userInfoVo.setPassword(userInfo.getResult().getPassword());
        userInfoVoResponse.setResult(userInfoVo);
        return userInfoVoResponse;
    }
}

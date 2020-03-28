package web.manager;

import cart.response.Response;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import web.query.UserLoginQuery;
import web.response.UserInfoVo;
import weimob.cart.api.facade.UserInfoServiceReadFacade;
import weimob.cart.api.request.UserInfoRequest;
import weimob.cart.api.response.UserInfo;

/**
 * @Author: 老张
 * @Date: 2020/3/28
 */
@Component
public class UserManager {
    @Reference
    private UserInfoServiceReadFacade userInfoServiceReadFacade;

    public Response<UserInfoVo> getUserInfo(UserLoginQuery query){
        Response<UserInfoVo> userInfoVoResponse=new Response<>();
        UserInfoVo userInfoVo = new UserInfoVo();
        UserInfoRequest userInfoRequest = new UserInfoRequest();
        userInfoRequest.setPassword(query.getPassword());
        userInfoRequest.setPhoneNumber(query.getPhoneNumber());
        Response<UserInfo> userInfo = userInfoServiceReadFacade.getUserInfo(userInfoRequest);
        if(userInfo.isSuccess()) {
            userInfoVo.setUserId(userInfo.getResult().getUserId());
            userInfoVo.setUserName(userInfo.getResult().getUserName());
            userInfoVoResponse.setResult(userInfoVo);
            return userInfoVoResponse;
        }
        userInfoVoResponse.setError(userInfo.getCode(),userInfo.getError());
        return userInfoVoResponse;
    }
}

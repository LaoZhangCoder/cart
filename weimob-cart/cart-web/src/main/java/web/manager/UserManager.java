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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: 老张
 * @Date: 2020/3/28
 */
@Component
public class UserManager {
    @Reference
    private UserInfoServiceReadFacade userInfoServiceReadFacade;

    @Autowired
    private CartManager cartManager;

    public Response<UserInfoVo> getUserInfo(UserLoginQuery query, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Response<UserInfoVo> userInfoVoResponse = new Response<>();
        UserInfoVo userInfoVo = new UserInfoVo();
        UserInfoRequest userInfoRequest = new UserInfoRequest();
        userInfoRequest.setPassword(query.getPassword());
        userInfoRequest.setPhoneNumber(query.getPhoneNumber());
        Response<UserInfo> userInfo = userInfoServiceReadFacade.getUserInfo(userInfoRequest);
        if (userInfo.isSuccess()) {
            userInfoVo.setUserId(userInfo.getResult().getUserId());
            userInfoVo.setUserName(userInfo.getResult().getUserName());
            userInfoVoResponse.setResult(userInfoVo);
            Cookie[] cookies = new Cookie[2];
            cookies[0] = new Cookie("userInfo", userInfoVo.getUserId() + ":" + userInfoVo.getUserName());
            cookies[0].setPath("/");
            cookies[0].setMaxAge(1000);
            response.addCookie(cookies[0]);
            //合并cookie
            AtomicBoolean excludeCart = cartManager.isExcludeCart(request.getCookies());
            if (excludeCart.get()) {
                cookies[1] = getCartListCookie(request.getCookies());
                cartManager.mergeCookieCart(response, null, cookies);
            }
            return userInfoVoResponse;
        }
        userInfoVoResponse.setError(userInfo.getCode(), userInfo.getError());
        return userInfoVoResponse;
    }

    private Cookie getCartListCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if ("cartList".equals(cookie.getName())) {
                return cookie;
            }
        }
        return null;
    }
}

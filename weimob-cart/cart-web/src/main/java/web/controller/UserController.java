package web.controller;

import cart.response.Response;
import com.alibaba.dubbo.config.annotation.Reference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.constants.CookieConstant;
import web.constants.RedisConstant;
import web.converter.MergeCartInfoRequestConverter;
import web.converter.UserInfoRequestConverter;
import web.converter.UserInfoVoResponseConverter;
import web.query.UserLoginQuery;
import web.response.UserInfoVo;
import web.utils.CookieUtils;
import web.utils.RedisClientUtils;
import weimob.cart.api.facade.CartInfoServiceWriteFacade;
import weimob.cart.api.facade.UserInfoServiceReadFacade;
import weimob.cart.api.request.MergeCartInfoRequest;
import weimob.cart.api.request.UserInfoRequest;
import weimob.cart.api.response.UserInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: 老张
 * @Date: 2020/3/28
 */
@RestController
@RequestMapping(value = "/api/user/")
@Slf4j
public class UserController {
    @Reference
    private UserInfoServiceReadFacade userInfoServiceReadFacade;
    @Reference
    private CartInfoServiceWriteFacade cartInfoServiceWriteFacade;

    @Autowired
    private RedisClientUtils redisClientUtils;

    @PostMapping(value = "login")
    public Response<UserInfoVo> checkLogin(@RequestBody UserLoginQuery query, HttpServletRequest request, HttpServletResponse response) {
        Response<UserInfoVo> userInfoVoResponse = new Response<>();
        UserInfoRequest userInfoRequest = UserInfoRequestConverter.getUserInfoRequest(query);
        Response<UserInfo> userInfo = userInfoServiceReadFacade.getUserInfo(userInfoRequest);
        if (userInfo.isSuccess()) {
            //用户登录成功即合并本地cookie购物车数据
            return getUserInfoVoResponse(request, response, userInfoVoResponse, userInfo);
        }
        userInfoVoResponse.setError(userInfo.getError());
        return userInfoVoResponse;
    }

    @GetMapping(value = "check")
    public Response<String> isLogin(HttpServletRequest request) {
        boolean isLogin = CookieUtils.isLogin(request.getCookies());
        if (isLogin) {
            UserInfoRequest userInfoRequest = UserInfoRequestConverter.getUserInfoRequest(CookieUtils.getUserIdByCookie(request.getCookies()), CookieUtils.getPasswordByCookie(request.getCookies()));
            Response<UserInfo> userInfo = userInfoServiceReadFacade.getUserInfo(userInfoRequest);
            if (userInfo.isSuccess()) {
                return Response.ok("user login is success!");
            }
            return Response.fail("userName or password is error!");
        }
        return Response.fail("please sign in!");
    }

    private Response<UserInfoVo> getUserInfoVoResponse(HttpServletRequest request, HttpServletResponse response, Response<UserInfoVo> userInfoVoResponse, Response<UserInfo> userInfo) {
        UserInfoVo userInfoVo = UserInfoVoResponseConverter.getUserInfoVoResponse(userInfo, userInfoVoResponse).getResult();
        //登录成功添加cookie
        CookieUtils.addCookie(response, userInfoVo);
        //合并cookie
        boolean excludeCart = CookieUtils.isExcludeCookieCart(request.getCookies());
        if (excludeCart) {
            redisClientUtils.deleteCache(RedisConstant.KEY,userInfoVo.getUserId());
            MergeCartInfoRequest mergeCartInfoRequest = MergeCartInfoRequestConverter.mergeCartInfoRequest(userInfoVo.getUserId(), CookieUtils.getCartCookieValue(request.getCookies()));
            Response<Boolean> result = cartInfoServiceWriteFacade.mergeCartInfo(mergeCartInfoRequest);
            if (result.getResult()) {
                CookieUtils.delCartCookie(response, CookieConstant.COOKIE_CART_NAME);
            }
        }
        return userInfoVoResponse;
    }


}

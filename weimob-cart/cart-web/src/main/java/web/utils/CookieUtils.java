package web.utils;

import cart.untils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import web.constants.CookieConstant;
import web.response.UserInfoVo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @Author: 老张
 * @Date: 2020/4/10
 */
@Slf4j
public class CookieUtils {
    public static boolean isExcludeCookieCart(Cookie[] cookies) {
        //判断是否存在本地Cookie购物车
        AtomicBoolean isExcludeCart = new AtomicBoolean(false);
        if (cookies != null && cookies.length > 0) {
            Arrays.stream(cookies).filter(cookie -> CookieConstant.COOKIE_CART_NAME.equals(cookie.getName())).map(cookie -> true).forEach(isExcludeCart::set);
        }
        return isExcludeCart.get();
    }

    /**
     * @param
     * @return
     * @desciption 判断用户是否登录
     */
    public static boolean isLogin(Cookie[] cookies) {
        if (cookies == null || cookies.length <= 0) {
            return false;
        } else {
            List<Boolean> collect = Arrays.stream(cookies).filter(cookie -> CookieConstant.COOKIE_USER_NAME.equals(cookie.getName()))
                    .map(cookie -> true).collect(Collectors.toList());
            if (collect.isEmpty()) {
                return false;
            }
            return true;
        }
    }

    public static String getCartCookieValue(Cookie[] cookies) {
        //获取cookie中购物车列表
        return Arrays.stream(cookies).filter(cookie -> CookieConstant.COOKIE_CART_NAME.equals(cookie.getName())).map(Cookie::getValue).collect(Collectors.toList()).get(0);
    }

    public static String getUserIdByCookie(Cookie[] cookies) {
        if (cookies == null || cookies.length <= 0) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (CookieConstant.COOKIE_USER_NAME.equals(cookie.getName())) {
                return cookie.getValue().substring(0, cookie.getValue().indexOf(":"));
            }
        }
        return null;
    }

    public static void delCartCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static void addCookie(HttpServletResponse response, UserInfoVo userInfoVo) {
        Cookie cookie = new Cookie(CookieConstant.COOKIE_USER_NAME, userInfoVo.getUserId() + ":" + userInfoVo.getUserName());
        cookie.setPath("/");
        cookie.setMaxAge(CookieConstant.COOKIE_MAX_AGE_ONE_DAY);
        response.addCookie(cookie);
    }

    public static void addCookie(HttpServletResponse response, String value) {
        try {
            String encodeCookie = URLEncoder.encode(value, "utf-8");
            Cookie cookie = new Cookie(CookieConstant.COOKIE_CART_NAME, encodeCookie);
            cookie.setPath("/");
            cookie.setMaxAge(CookieConstant.COOKIE_MAX_AGE_ONE_DAY);
            response.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            log.warn("\n 方法:{} \n 入参: {} \n", "web.utils.CookieUtils.addCookie(javax.servlet.http.HttpServletResponse", value);
            log.warn("错误信息:{}", e.getMessage());
        }
    }
}

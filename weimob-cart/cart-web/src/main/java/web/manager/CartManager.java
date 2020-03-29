package web.manager;

import cart.response.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Component;
import web.query.CartAddQuery;
import web.response.CartInfoVo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: 老张
 * @Date: 2020/3/29
 */
@Component
public class CartManager {
    public Response<String> addGoods(HttpServletRequest request, CartAddQuery cartAddQuery) throws Exception {
        Response<String> stringResponse = new Response<>();
        Cookie[] cookies = request.getCookies();
        AtomicBoolean isLogin = isLogin(cookies);
        if (isLogin.get()) {
            //已登录合并cookie购物车到数据库并记得清除
        } else {
            //判断是否存在本地购物车
            AtomicBoolean isExcludeCart = isExcludeCart(cookies);
            //未登录添加购物车到cookie
            addCartToCookie(cartAddQuery, request.getCookies(), stringResponse, isExcludeCart);
        }
        return stringResponse;
    }

    private AtomicBoolean isExcludeCart(Cookie[] cookies) {
        //判断是否存在本地Cookie购物车
        AtomicBoolean isExcludeCart = new AtomicBoolean(false);
        if (cookies != null && cookies.length > 0) {
            Arrays.stream(cookies).filter(cookie -> "cartList".equals(cookie.getName())).map(cookie -> true).forEach(isExcludeCart::set);
        }
        return isExcludeCart;
    }

    private AtomicBoolean isLogin(Cookie[] cookies) {
        AtomicBoolean isLogin = new AtomicBoolean(false);
        //判断用户是否登录
        if (cookies == null || cookies.length == 0) {
            isLogin.set(false);
        } else {
            //判断是否存在用户
            Arrays.stream(cookies).filter(cookie -> "userInfo".equals(cookie.getName())).map(cookie -> true).forEach(isLogin::set);
        }
        return isLogin;
    }

    private void addCartToCookie(CartAddQuery cartAddQuery, Cookie[] cookies, Response<String> stringResponse, AtomicBoolean isExcludeCart) throws java.io.IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        //未登录,是否cookie当中已经存在购物车
        if (isExcludeCart.get()) {
            excludeCart(cartAddQuery, cookies, stringResponse, objectMapper);
        } else {
            notIncludeCart(cartAddQuery, stringResponse, objectMapper);
        }
    }

    private void notIncludeCart(CartAddQuery cartAddQuery, Response<String> stringResponse, ObjectMapper objectMapper) throws JsonProcessingException {
        //用户未登录且第一次添加商品到购物车所走逻辑
        CartInfoVo cartInfoVo = new CartInfoVo();
        cartInfoVo.setChecked(true);
        cartInfoVo.setSkuId(cartAddQuery.getId());
        cartInfoVo.setCount(1);
        cartInfoVo.setGoodsNum(cartAddQuery.getGoodsNum());
        List<CartInfoVo> cartInfoVos = new ArrayList<>();
        cartInfoVos.add(cartInfoVo);
        String s = objectMapper.writeValueAsString(cartInfoVos);
        stringResponse.setResult(s);
    }

    private void excludeCart(CartAddQuery cartAddQuery, Cookie[] cookies, Response<String> stringResponse, ObjectMapper objectMapper) throws java.io.IOException {
      //用户为登录且重复添加商品到购物车所走的逻辑
        String cartJsonObject = "";
        for (Cookie cookie : cookies) {
            if ("cartList".equals(cookie.getName())) {
                cartJsonObject = getCartCookieJsonToObject(cartAddQuery, objectMapper, cookie);
            }
        }
        stringResponse.setResult(cartJsonObject);
    }

    private String getCartCookieJsonToObject(CartAddQuery cartAddQuery, ObjectMapper objectMapper, Cookie cookie) throws java.io.IOException {
        String cartJsonObject;
        String value = cookie.getValue();
        String decode = URLDecoder.decode(value, "utf-8");
        String s = StringEscapeUtils.unescapeJava(decode);
        String substring = s.substring(1, s.length() - 1);
        List<CartInfoVo> list = objectMapper.readValue(substring, new TypeReference<List<CartInfoVo>>() {
        });
        boolean isExclude = false;
        for (CartInfoVo infoVo : list) {
            //包含只需要数量加一
            if (infoVo.getSkuId().equals(cartAddQuery.getId())) {
                infoVo.setCount(infoVo.getCount() + 1);
                isExclude = true;
            }
        }
        //不包含则添加该商品到购物车
        if (!isExclude) {
            CartInfoVo cartInfoVo = new CartInfoVo();
            cartInfoVo.setChecked(true);
            cartInfoVo.setSkuId(cartAddQuery.getId());
            cartInfoVo.setCount(1);
            cartInfoVo.setGoodsNum(cartAddQuery.getGoodsNum());
            list.add(cartInfoVo);
        }
        cartJsonObject = objectMapper.writeValueAsString(list);
        return cartJsonObject;
    }
}

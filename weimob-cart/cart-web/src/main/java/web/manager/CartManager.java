package web.manager;

import cart.response.Response;
import com.alibaba.dubbo.config.annotation.Reference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import web.query.CartAddQuery;
import web.response.CartInfoVo;
import weimob.cart.api.facade.CartInfoServiceReadFacade;
import weimob.cart.api.facade.CartInfoServiceWriteFacade;
import weimob.cart.api.request.CartInfosSaveRequest;
import weimob.cart.api.response.CartInfo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @Author: 老张
 * @Date: 2020/3/29
 */
@Component
public class CartManager {

    @Reference
    private CartInfoServiceWriteFacade cartInfoServiceWriteFacade;

    @Reference
    private CartInfoServiceReadFacade cartInfoServiceReadFacadeFacade;


    public Response<String> addGoods(HttpServletRequest request, HttpServletResponse response, CartAddQuery cartAddQuery) throws Exception {
        Response<String> stringResponse = new Response<>();
        Cookie[] cookies = request.getCookies();
        AtomicBoolean isLogin = isLogin(cookies);
        //判断是否存在本地购物车
        AtomicBoolean isExcludeCart = isExcludeCart(cookies);
        if (isLogin.get()) {
            List<CartInfosSaveRequest> requests;
            //两个逻辑登陆后本地是否存在缓存在cookie的购物车
            if (isExcludeCart.get()) {
                excludeCartCookie(response, cartAddQuery, cookies);
            } else {
                //本地cookie不含购物车数据，直接添加到数据库中
                notIncludeCookie(cartAddQuery, cookies);
            }

        } else {
            //未登录添加购物车到cookie
            addCartToCookie(cartAddQuery, request.getCookies(), stringResponse, isExcludeCart);
        }
        return stringResponse;
    }

    private void notIncludeCookie(CartAddQuery cartAddQuery, Cookie[] cookies) {
        List<CartInfosSaveRequest> requests;
        //转换参数
        CartInfosSaveRequest cartInfosSaveRequest = changeRequestParam(cookies);
        cartInfosSaveRequest.setSkuId(cartAddQuery.getId());
        //获取该用户下的所有购物车
        List<CartInfo> result = cartInfoServiceReadFacadeFacade.listCartInfos(cartInfosSaveRequest.getUserId()).getResult();
        //数据库中是否包含该购物车
        AtomicBoolean isExist = new AtomicBoolean(false);
        result.stream().filter(cartInfo -> cartInfosSaveRequest.getSkuId().equals(cartInfo.getSkuId())).map(cartInfo -> true).forEach(isExist::set);
        //包含两个逻辑一个是购物车表中是否包含该商品，包含则数量加一不包含则新建加入购物车,和拼接远程接口所需参数
        requests = getCartInfosSaveRequests(cartInfosSaveRequest, result, isExist);

        cartInfoServiceWriteFacade.saveCartInfos(requests);
    }

    private void excludeCartCookie(HttpServletResponse response, CartAddQuery cartAddQuery, Cookie[] cookies) throws java.io.IOException {
        List<CartInfosSaveRequest> requests;
        List<CartInfoVo> cartCookieList = getCartCookieValueToObject(cookies);
        //获取该用户下的所有购物车
        String userId = getUserId(cookies);
        List<CartInfo> cartList = cartInfoServiceReadFacadeFacade.listCartInfos(userId).getResult();
        //相同的商品即数量相加
        sameCartNumIncrease(cartCookieList, cartList);
        requests = cartList.stream().map(cartInfo -> {
            CartInfosSaveRequest saveRequest = new CartInfosSaveRequest();
            saveRequest.setSkuId(cartInfo.getSkuId());
            saveRequest.setUserId(userId);
            saveRequest.setChecked(1);
            saveRequest.setCount(cartInfo.getCount());
            return saveRequest;
        }).collect(Collectors.toList());
        //新加商品添加到购物车
        notSameCartAddToList(requests, cartCookieList, userId, cartList);
        //合并完后删除本低cookie
        delCartCookie(response);
        //请求参数带的商品是否添加到购物车还是只是数量加一
        requestCartIfAddCart(cartAddQuery, requests, userId);

        cartInfoServiceWriteFacade.saveCartInfos(requests);
    }

    private void requestCartIfAddCart(CartAddQuery cartAddQuery, List<CartInfosSaveRequest> requests, String userId) {
        boolean isExclude = false;
        for (CartInfosSaveRequest cartInfosSaveRequest : requests) {
            if (cartInfosSaveRequest.getSkuId().equals(cartAddQuery.getId())) {
                isExclude = true;
                cartInfosSaveRequest.setCount(cartInfosSaveRequest.getCount() + 1);
            }
        }
        if (!isExclude) {
            CartInfosSaveRequest saveRequest = new CartInfosSaveRequest();
            saveRequest.setSkuId(cartAddQuery.getId());
            saveRequest.setUserId(userId);
            saveRequest.setChecked(1);
            saveRequest.setCount(1);
            requests.add(saveRequest);
        }
    }

    private void delCartCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("cartList", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private void notSameCartAddToList(List<CartInfosSaveRequest> requests, List<CartInfoVo> cartCookieList, String userId, List<CartInfo> cartList) {
        Set<Integer> cartIds = cartList.stream().map(CartInfo::getSkuId).collect(Collectors.toSet());
        for (CartInfoVo cartInfoVo : cartCookieList) {
            if (!cartIds.contains(cartInfoVo.getSkuId())) {
                CartInfosSaveRequest saveRequest = new CartInfosSaveRequest();
                saveRequest.setSkuId(cartInfoVo.getSkuId());
                saveRequest.setUserId(userId);
                saveRequest.setChecked(1);
                saveRequest.setCount(cartInfoVo.getCount());
                requests.add(saveRequest);
            }
        }
    }

    private void sameCartNumIncrease(List<CartInfoVo> cartCookieList, List<CartInfo> cartList) {
        for (CartInfo cartInfo : cartList) {
            for (CartInfoVo cartInfoVo : cartCookieList) {
                if (cartInfoVo.getSkuId().equals(cartInfo.getSkuId())) {
                    cartInfo.setCount(cartInfo.getCount() + cartInfoVo.getCount());
                }
            }

        }
    }

    private List<CartInfoVo> getCartCookieValueToObject(Cookie[] cookies) throws java.io.IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Cookie cartCookie;
        cartCookie = getStringResponse(cookies);
        List<CartInfoVo> list = getCookieJsonToObject(objectMapper, cartCookie);
        return list;
    }

    private Cookie getStringResponse(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if ("cartList".equals(cookie.getName())) {
                return cookie;
            }
        }
        return null;
    }

    private List<CartInfosSaveRequest> getCartInfosSaveRequests(CartInfosSaveRequest cartInfosSaveRequest, List<CartInfo> result, AtomicBoolean isExist) {
        List<CartInfosSaveRequest> requests;
        if (isExist.get()) {
            result.stream().forEach(cartInfo -> {
                if (cartInfo.getSkuId().equals(cartInfosSaveRequest.getSkuId())) {
                    cartInfo.setCount(cartInfo.getCount() + 1);
                    cartInfo.setChecked(1);
                }
            });

        } else {
            CartInfo cartInfo = new CartInfo();
            BeanUtils.copyProperties(cartInfosSaveRequest, cartInfo);
            result.add(cartInfo);
        }
        requests = result.stream().map(cartInfo -> {
            CartInfosSaveRequest saveRequest = new CartInfosSaveRequest();
            saveRequest.setCount(cartInfo.getCount());
            saveRequest.setChecked(1);
            saveRequest.setUserId(cartInfo.getUserId());
            saveRequest.setSkuId(cartInfo.getSkuId());
            return saveRequest;
        }).collect(Collectors.toList());
        return requests;
    }

    private CartInfosSaveRequest changeRequestParam(Cookie[] cookies) {
        CartInfosSaveRequest cartInfosSaveRequest = new CartInfosSaveRequest();
        String userId = getUserId(cookies);
        cartInfosSaveRequest.setUserId(userId);
        cartInfosSaveRequest.setChecked(1);
        cartInfosSaveRequest.setCount(1);
        return cartInfosSaveRequest;
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

    private String getUserId(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if ("userInfo".equals(cookie.getName())) {
                return cookie.getValue().substring(0, cookie.getValue().indexOf(":"));
            }
        }
        return null;
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
        cartInfoVo.setChecked(1);
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
        List<CartInfoVo> list = getCookieJsonToObject(objectMapper, cookie);
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
            cartInfoVo.setChecked(1);
            cartInfoVo.setSkuId(cartAddQuery.getId());
            cartInfoVo.setCount(1);
            cartInfoVo.setGoodsNum(cartAddQuery.getGoodsNum());
            list.add(cartInfoVo);
        }
        cartJsonObject = objectMapper.writeValueAsString(list);
        return cartJsonObject;
    }

    private List<CartInfoVo> getCookieJsonToObject(ObjectMapper objectMapper, Cookie cookie) throws java.io.IOException {
        String value = cookie.getValue();
        String decode = URLDecoder.decode(value, "utf-8");
        String s = StringEscapeUtils.unescapeJava(decode);
        String substring = s.substring(1, s.length() - 1);
        return objectMapper.readValue(substring, new TypeReference<List<CartInfoVo>>() {
        });
    }
}

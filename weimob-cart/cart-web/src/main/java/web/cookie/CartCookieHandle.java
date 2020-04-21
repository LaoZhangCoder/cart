package web.cookie;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import web.query.CartAddQuery;
import web.query.CartUpdateQuery;
import web.response.CartInfoVo;
import web.utils.CookieUtils;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: 老张
 * @Date: 2020/4/11
 */
@Component
@Slf4j
public class CartCookieHandle {
    static ObjectMapper objectMapper = new ObjectMapper();

    public String addCartToCookie(CartAddQuery cartAddQuery, Cookie[] cookies, boolean isExcludeCart) {
        //未登录,是否cookie当中已经存在购物车
        if (isExcludeCart) {
            return excludeCart(cartAddQuery, cookies);
        } else {
            return notIncludeCart(cartAddQuery);
        }
    }

    private String excludeCart(CartAddQuery cartAddQuery, Cookie[] cookies) {
        //用户为登录且重复添加商品到购物车所走的逻辑
        String cookieValue = CookieUtils.getCartCookieValue(cookies);
        String cartJsonObject = getCartCookieJsonToObject(cartAddQuery, cookieValue);
        return cartJsonObject;
    }

    public List<CartInfoVo> getCookieJsonToObject(String cookieValue) {
        try {
            String decode = URLDecoder.decode(cookieValue, "utf-8");
            return objectMapper.readValue(decode, new TypeReference<List<CartInfoVo>>() {
            });
        } catch (IOException e) {
            log.error("json转换异常:{}", e.getMessage());
        }
        return null;
    }

    public String getObjectToStringJson(List<CartInfoVo> cartInfoVos) {
        try {
            String stringCookieValue = objectMapper.writeValueAsString(cartInfoVos);
            return stringCookieValue;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getCartCookieJsonToObject(CartAddQuery cartAddQuery, String cookieValue) {
        String cartJsonObject = null;
        List<CartInfoVo> list = getCookieJsonToObject(cookieValue);
        boolean isExclude = false;
        for (CartInfoVo infoVo : list) {
            //包含只需要数量加一
            if (infoVo.getSkuId().equals(cartAddQuery.getId())) {
                if (infoVo.getCount() + 1 > cartAddQuery.getGoodsNum()) {
                    infoVo.setCount(cartAddQuery.getGoodsNum());
                    isExclude = true;
                    break;
                }
                infoVo.setCount(infoVo.getCount() + 1);
                isExclude = true;
            }
        }
        //不包含则添加该商品到购物车
        if (!isExclude) {
            CartInfoVo cartInfoVo = CartInfoVo.builder().checked(1).
                    skuId(cartAddQuery.getId()).
                    count(1).
                    goodsNum(cartAddQuery.getGoodsNum()).
                    goodsName(cartAddQuery.getGoodsName()).
                    goodsPrice(cartAddQuery.getGoodsPrice()).
                    build();
            list.add(cartInfoVo);
        }
        try {
            cartJsonObject = objectMapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return cartJsonObject;
    }

    private String notIncludeCart(CartAddQuery cartAddQuery) {
        //用户未登录且第一次添加商品到购物车所走逻辑
        CartInfoVo cartInfoVo = CartInfoVo.builder().checked(1).
                goodsName(cartAddQuery.getGoodsName()).skuId(cartAddQuery.getId()).
                count(1).goodsNum(cartAddQuery.getGoodsNum()).
                goodsPrice(cartAddQuery.getGoodsPrice()).build();
        List<CartInfoVo> cartInfoVos = new ArrayList<>();
        cartInfoVos.add(cartInfoVo);
        try {
            String s = objectMapper.writeValueAsString(cartInfoVos);
            return s;
        } catch (JsonProcessingException e) {
            log.warn("\n 方法:{} \n 入参: {} \n", "web.cookie.CartCookieHandle.notIncludeCart", cartAddQuery.toString());
            log.warn("错误信息:{}", e.getMessage());
        }
        return null;
    }

    public String updateCookieValue(List<CartInfoVo> result, CartUpdateQuery cartUpdateQuery) {
        if (cartUpdateQuery.getIsChecked()) {
            if (cartUpdateQuery.getIsAllChecked()) {
                boolean flag = false;
                for (CartInfoVo infoVo : result) {
                    if (infoVo.getChecked().equals(0)) {
                        flag = true;
                    }
                }
                if (flag) {
                    result.forEach(cartInfoVo -> cartInfoVo.setChecked(1));
                } else {
                    result.forEach(cartInfoVo -> cartInfoVo.setChecked(0));
                }
                return getObjectToStringJson(result);
            }
            result.forEach(cartInfoVo -> {
                if (cartInfoVo.getSkuId().equals(cartUpdateQuery.getSkuId())) {
                    cartInfoVo.setChecked(cartInfoVo.getChecked() == 1 ? 0 : 1);
                }
            });
            return getObjectToStringJson(result);
        } else if (cartUpdateQuery.getIsCount()) {
            result.forEach(cartInfoVo -> {
                if (cartInfoVo.getSkuId().equals(cartUpdateQuery.getSkuId())) {
                    cartInfoVo.setCount(cartUpdateQuery.getCount());
                }
            });
            return getObjectToStringJson(result);

        }
        return getObjectToStringJson(result);
    }

    public String deleteCartById(List<CartInfoVo> result, Integer id) {
        CartInfoVo res = null;
        for (CartInfoVo cartInfoVo : result) {
            if (cartInfoVo.getSkuId().equals(id)) {
                res = cartInfoVo;
            }
        }
        result.remove(res);
        return getObjectToStringJson(result);
    }

    public String deleteCartByIds(List<CartInfoVo> result, List<Integer> ids) {
        List<CartInfoVo> cartInfoVos = result.stream().filter(cartInfoVo -> !ids.contains(cartInfoVo.getSkuId())).collect(Collectors.toList());
        return getObjectToStringJson(cartInfoVos);
    }
}
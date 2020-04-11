package web.cookie;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import web.query.CartAddQuery;
import web.response.CartInfoVo;
import web.utils.CookieUtils;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 老张
 * @Date: 2020/4/11
 */
@Component
@Slf4j
public class CartCookieHandle {
    final static ObjectMapper objectMapper = new ObjectMapper();

    public String addCartToCookie(CartAddQuery cartAddQuery, Cookie[] cookies, boolean isExcludeCart) {
        //未登录,是否cookie当中已经存在购物车
        if (isExcludeCart) {
            return excludeCart(cartAddQuery, cookies, objectMapper);
        } else {
            return notIncludeCart(cartAddQuery, objectMapper);
        }
    }

    private String excludeCart(CartAddQuery cartAddQuery, Cookie[] cookies, ObjectMapper objectMapper) {
        //用户为登录且重复添加商品到购物车所走的逻辑
        String cookieValue = CookieUtils.getCartCookieValue(cookies);
        String cartJsonObject = getCartCookieJsonToObject(cartAddQuery, objectMapper, cookieValue);
        return cartJsonObject;
    }

    private List<CartInfoVo> getCookieJsonToObject(ObjectMapper objectMapper, String cookieValue) {
        try {
            String decode = URLDecoder.decode(cookieValue, "utf-8");
            return objectMapper.readValue(decode, new TypeReference<List<CartInfoVo>>() {
            });
        } catch (IOException e) {
            log.error("json转换异常:{}", e.getMessage());
        }
        return  null;
    }

    private String getCartCookieJsonToObject(CartAddQuery cartAddQuery, ObjectMapper objectMapper, String cookieValue) {
        String cartJsonObject = null;
        List<CartInfoVo> list = getCookieJsonToObject(objectMapper, cookieValue);
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
            CartInfoVo cartInfoVo = CartInfoVo.builder().checked(1).
                    skuId(cartAddQuery.getId()).
                    count(1).
                    goodsNum(cartAddQuery.getGoodsNum()).
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

    private String notIncludeCart(CartAddQuery cartAddQuery, ObjectMapper objectMapper) {
        //用户未登录且第一次添加商品到购物车所走逻辑
        CartInfoVo cartInfoVo = CartInfoVo.builder().checked(1).skuId(cartAddQuery.getId()).count(1).goodsNum(cartAddQuery.getGoodsNum()).build();
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
}

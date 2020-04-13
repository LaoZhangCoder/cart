package web.cookie;

import org.junit.Test;
import web.response.CartInfoVo;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @Author: 老张
 * @Date: 2020/4/13
 */
public class CartCookieHandleTest {

    @Test
    public void deleteCartById() {
        CartCookieHandle cartCookieHandle = new CartCookieHandle();
        String str = "[{\"id\":null,\"userId\":null,\"skuId\":1,\"count\":4,\"goodsNum\":19,\"checked\":0,\"goodsName\":\"裤子\",\"goodsPrice\":11.1},{\"id\":null,\"userId\":null,\"skuId\":2,\"count\":1,\"goodsNum\":12,\"checked\":1,\"goodsName\":\"袜子\",\"goodsPrice\":100},{\"id\":null,\"userId\":null,\"skuId\":3,\"count\":1,\"goodsNum\":13,\"checked\":1,\"goodsName\":\"电脑\",\"goodsPrice\":1111},{\"id\":null,\"userId\":null,\"skuId\":4,\"count\":1,\"goodsNum\":123,\"checked\":1,\"goodsName\":\"游戏机\",\"goodsPrice\":1233}]" ;
        List<CartInfoVo> cookieJsonToObject = cartCookieHandle.getCookieJsonToObject(str);
        cartCookieHandle.deleteCartById(cookieJsonToObject,1);
    }
}
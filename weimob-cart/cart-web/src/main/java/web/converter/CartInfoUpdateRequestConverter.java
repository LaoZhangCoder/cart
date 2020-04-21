package web.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import web.query.CartUpdateQuery;
import web.utils.CookieUtils;
import weimob.cart.api.request.CartInfoUpdateRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: 老张
 * @Date: 2020/4/12
 */
@Slf4j
public class CartInfoUpdateRequestConverter {
    public static CartInfoUpdateRequest getCartInfoUpdateRequest(HttpServletRequest request, CartUpdateQuery cartUpdateQuery) {
        String userId = CookieUtils.getUserIdByCookie(request.getCookies());
        CartInfoUpdateRequest cartInfoUpdateRequest = new CartInfoUpdateRequest();
        BeanUtils.copyProperties(cartUpdateQuery, cartInfoUpdateRequest);
        cartInfoUpdateRequest.setUserId(userId);
        cartInfoUpdateRequest.setSkuId(cartUpdateQuery.getSkuId());
        return cartInfoUpdateRequest;
    }
}

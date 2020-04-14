package web.converter;

import weimob.cart.api.request.CartInfoListRemoveRequest;

import java.util.List;

/**
 * @Author: 老张
 * @Date: 2020/4/14
 */
public class CartInfoListRemoveRequestConverter {
    public static CartInfoListRemoveRequest cartInfoListRemoveRequest(String userId, List<Integer> ids) {
        CartInfoListRemoveRequest cartInfoListRemoveRequest = new CartInfoListRemoveRequest();
        cartInfoListRemoveRequest.setSkuIds(ids);
        cartInfoListRemoveRequest.setUserId(userId);
        return cartInfoListRemoveRequest;
    }
}

package web.converter;

import weimob.cart.api.request.CartInfoDeleteRequest;

/**
 * @Author: 老张
 * @Date: 2020/4/13
 */
public class CartInfoDeleteRequestConverter {
    public static CartInfoDeleteRequest cartInfoDeleteRequest(String userId,Integer skuId){
        CartInfoDeleteRequest cartInfoDeleteRequest = new CartInfoDeleteRequest();
        cartInfoDeleteRequest.setUserId(userId);
        cartInfoDeleteRequest.setSkuId(skuId);
        return cartInfoDeleteRequest;
    }
}

package weimob.cart.api.facade;

import cart.response.Response;
import weimob.cart.api.request.CartInfoUpdateRequest;
import weimob.cart.api.request.CartInfosSaveRequest;

import java.util.List;

/**
 * @Author: 老张
 * @Date: 2020/3/31
 */
public interface CartInfoServiceWriteFacade {
    /**
     *
     * @param requests
     * @return
     * @descition 保存购物车
     */
    Response<Boolean> saveCartInfos(List<CartInfosSaveRequest> requests);

    /**
     *
     * @param request
     * @return
     * @descition 修改购物车的部分信息
     */
    Response<String> updateCartInfo(CartInfoUpdateRequest request);

    /**
     *
     * @param cartId
     * @return
     * @desciton 删除购物车
     */
    Response<String> deleteCart(Integer cartId);
}

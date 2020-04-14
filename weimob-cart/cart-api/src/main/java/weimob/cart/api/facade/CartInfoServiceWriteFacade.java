package weimob.cart.api.facade;

import cart.response.Response;
import weimob.cart.api.request.CartInfoDeleteRequest;
import weimob.cart.api.request.CartInfoListRemoveRequest;
import weimob.cart.api.request.CartInfoUpdateRequest;
import weimob.cart.api.request.MergeCartInfoRequest;

/**
 * @Author: 老张
 * @Date: 2020/3/31
 */
public interface CartInfoServiceWriteFacade {
    /**
     * @param request
     * @return
     * @desciption 合并cookie当中的购物车信息到数据库
     */
    Response<Boolean> mergeCartInfo(MergeCartInfoRequest request);

    /**
     * @param request
     * @return
     * @descition 修改购物车的部分信息
     */
    Response<String> updateCartInfo(CartInfoUpdateRequest request);

    /**
     * @param request
     * @return
     * @desciton 删除购物车
     */
    Response<String> deleteCart(CartInfoDeleteRequest request);

    /**
     * @param request
     * @return
     * @desciption 删除购物车集合
     */
    Response<String> removeCartList(CartInfoListRemoveRequest request);

}

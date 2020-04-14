package weimob.cart.server.service;

import weimob.cart.api.request.CartInfoDeleteRequest;
import weimob.cart.api.request.CartInfoListRemoveRequest;
import weimob.cart.api.request.CartInfoUpdateRequest;
import weimob.cart.api.request.MergeCartInfoRequest;
import weimob.cart.server.domain.dto.CartInfoDto;
import weimob.cart.server.query.CartInfosQuery;

import java.util.List;

/**
 * @Author: 老张
 * @Date: 2020/3/31
 */
public interface CartService {
    /**
     * @param cartInfosQuery
     * @return
     */

    List<CartInfoDto> listCartInfo(CartInfosQuery cartInfosQuery);

    /**
     * @param request
     * @return
     * @desciption 合并购物车
     */
    Boolean mergeCartInfo(MergeCartInfoRequest request);

    /**
     * @param request
     * @return string
     * @desciption 更新购物车部分信息
     */
    String updateCartInfo(CartInfoUpdateRequest request);

    /**
     * @param request
     * @return
     */

    String deleteCart(CartInfoDeleteRequest request);

    /**
     *
     * @param request
     * @return
     * @desicption: 删除购物车集合
     */
    String removeCartList(CartInfoListRemoveRequest request);

}

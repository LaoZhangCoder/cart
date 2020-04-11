package weimob.cart.server.service;

import cart.response.Response;
import weimob.cart.api.request.CartInfoUpdateRequest;
import weimob.cart.api.request.MergeCartInfoRequest;
import weimob.cart.server.domain.dto.CartInfoDto;
import weimob.cart.server.query.CartInfoSaveQuery;
import weimob.cart.server.query.CartInfosQuery;

import java.util.List;

/**
 * @Author: 老张
 * @Date: 2020/3/31
 */
public interface CartService {
    /**
     * @param cartInfoSaveQuery
     * @return boolean
     * @desception 添加购物车到数据表中
     */
    Boolean insertCartInfo(List<CartInfoSaveQuery> cartInfoSaveQuery);

    /**
     *
     * @param cartInfosQuery
     * @return
     */

    List<CartInfoDto> listCartInfo(CartInfosQuery cartInfosQuery);

    /**
     *
     * @param request
     * @return
     * @desciption 合并购物车
     */
    Boolean mergeCartInfo(MergeCartInfoRequest request);

    /**
     *
     * @param request
     * @return string
     * @desciption 更新购物车部分信息
     */
    Response<String> updateCartInfo(CartInfoUpdateRequest request);

    /**
     *
     * @param cartId
     * @return
     */

    Response<String> deleteCart(Integer cartId);
}

package weimob.cart.server.service;

import weimob.cart.api.request.*;
import weimob.cart.server.domain.dto.CartInfoDto;
import weimob.cart.server.domain.dto.CompensateMessageDto;
import weimob.cart.server.query.CartInfosQuery;
import weimob.cart.server.query.CompensateMessageUpdateQuery;

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

    /**
     * 模拟订单服务
     * @param request
     * @return
     */
    String submitCartOrder(OrderRequest request);

    /**
     * 查询购物车消息表
     * @return
     */
    List<CompensateMessageDto> listCompensateMessageDto();

    /**
     * 更新购物车消息表
     * @param query
     * @return
     */
    String updateCompensateMessage(CompensateMessageUpdateQuery query);

}

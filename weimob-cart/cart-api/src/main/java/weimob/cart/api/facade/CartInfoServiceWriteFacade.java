package weimob.cart.api.facade;

import cart.response.Response;
import weimob.cart.api.request.*;
import weimob.cart.api.response.CompensateMessageInfo;

import java.util.List;

/**
 * @Author: 老张
 * @Date: 2020/3/31
 */
public interface CartInfoServiceWriteFacade {
    /**
     * 合并cookie当中的购物车信息到数据库
     *
     * @param request
     * @return
     */
    Response<Boolean> mergeCartInfo(MergeCartInfoRequest request);

    /**
     * 修改购物车的部分信息
     *
     * @param request
     * @return
     * @descition 修改购物车的部分信息
     */
    Response<String> updateCartInfo(CartInfoUpdateRequest request);

    /**
     * 删除购物车
     *
     * @param request
     * @return
     * @desciton 删除购物车
     */
    Response<String> deleteCart(CartInfoDeleteRequest request);

    /**
     * 删除购物车集合
     *
     * @param request
     * @return
     * @desciption 删除购物车集合
     */
    Response<String> removeCartList(CartInfoListRemoveRequest request);

    /**
     * 模拟订单服务
     *
     * @param request
     * @return
     */
    Response<String> submitCartOrder(OrderRequest request);

    /**
     * 获取下单成功时的购物车消息
     *
     * @return
     */
    Response<List<CompensateMessageInfo>> listCompensateMessageDto();

    /**
     * 更新购物车消息数据
     * @param query
     * @return
     */
    Response<String> updateCompensateMessage(CompensateMessageUpdateRequest query);

}

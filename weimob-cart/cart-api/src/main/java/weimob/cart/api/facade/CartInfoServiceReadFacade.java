package weimob.cart.api.facade;

import cart.response.Response;
import weimob.cart.api.response.CartInfo;
import java.util.List;

/**
 * @Author: 老张
 * @Date: 2020/3/31
 */
public interface CartInfoServiceReadFacade {
    /**
     * 获取用户购物车列表
     * @param userId
     * @return
     *
     */
    Response<List<CartInfo>> listCartInfos(String userId);
}

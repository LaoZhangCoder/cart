package weimob.cart.api.facade;

import cart.response.Response;
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
     */
    Response<Boolean> saveCartInfos(List<CartInfosSaveRequest> requests);

}

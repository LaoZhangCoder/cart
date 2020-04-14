package weimob.cart.api.facade.read;

import cart.response.Response;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import weimob.cart.api.facade.CartInfoServiceWriteFacade;
import weimob.cart.api.request.CartInfoDeleteRequest;
import weimob.cart.api.request.CartInfoListRemoveRequest;
import weimob.cart.api.request.CartInfoUpdateRequest;
import weimob.cart.api.request.MergeCartInfoRequest;
import weimob.cart.server.service.CartService;


/**
 * @Author: 老张
 * @Date: 2020/3/31
 */
@Service
@Component
public class CartInfoServiceWriteFacadeImpl implements CartInfoServiceWriteFacade {

    @Autowired
    private CartService cartService;

    @Override
    public Response<Boolean> mergeCartInfo(MergeCartInfoRequest request) {
        return Response.ok(cartService.mergeCartInfo(request));
    }

    @Override
    public Response<String> updateCartInfo(CartInfoUpdateRequest request) {
        return Response.ok(cartService.updateCartInfo(request));
    }

    @Override
    public Response<String> deleteCart(CartInfoDeleteRequest request) {
        return Response.ok(cartService.deleteCart(request));
    }

    @Override
    public Response<String> removeCartList(CartInfoListRemoveRequest request) {
        return Response.ok(cartService.removeCartList(request));
    }
}

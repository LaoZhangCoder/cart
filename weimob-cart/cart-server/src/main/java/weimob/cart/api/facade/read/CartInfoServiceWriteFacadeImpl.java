package weimob.cart.api.facade.read;

import cart.response.Response;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import weimob.cart.api.facade.CartInfoServiceWriteFacade;
import weimob.cart.api.request.CartInfoUpdateRequest;
import weimob.cart.api.request.CartInfosSaveRequest;
import weimob.cart.server.manager.CartInfoManager;
import weimob.cart.server.query.CartInfoSaveQuery;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: 老张
 * @Date: 2020/3/31
 */
@Service
@Component
public class CartInfoServiceWriteFacadeImpl implements CartInfoServiceWriteFacade {

    @Autowired
    private CartInfoManager cartInfoManager;

    @Override
    public Response<Boolean> saveCartInfos(List<CartInfosSaveRequest> requests) {
        Response<Boolean> response = new Response<>();
        List<CartInfoSaveQuery> saveQueryList = requests.stream().map(cartInfosSaveRequest -> {
            CartInfoSaveQuery cartInfoSaveQuery = new CartInfoSaveQuery();
            BeanUtils.copyProperties(cartInfosSaveRequest, cartInfoSaveQuery);
            return cartInfoSaveQuery;
        }).collect(Collectors.toList());

        Boolean isSuccess = cartInfoManager.saveCartInfo(saveQueryList);
        if (isSuccess) {
            response.setResult(true);
            response.setCode(HttpStatus.OK.value() + "");
        }
        response.setError(HttpStatus.INTERNAL_SERVER_ERROR.value() + "", "检查请求参数是否出错");
        return response;
    }

    @Override
    public Response<String> updateCartInfo(CartInfoUpdateRequest request) {
       return cartInfoManager.updateCartInfo(request);
    }
}

package weimob.cart.api.facade.read;

import cart.response.Response;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import weimob.cart.api.facade.CartInfoServiceReadFacade;
import weimob.cart.api.response.CartInfo;
import weimob.cart.server.domain.dto.CartInfoDto;
import weimob.cart.server.manager.CartInfoManager;
import weimob.cart.server.query.CartInfosQuery;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: 老张
 * @Date: 2020/3/31
 */
@Service
@Component
public class CartInfoServiceReadFacadeImpl implements CartInfoServiceReadFacade {
    @Autowired
    private CartInfoManager cartInfoManager;
    @Override
    public Response<List<CartInfo>> listCartInfos(String userId) {
        CartInfosQuery request=new CartInfosQuery();
        Response<List<CartInfo>> objectResponse = new Response<>();
        request.setUserId(userId);
        List<CartInfoDto> cartInfoDtoList = cartInfoManager.listCartInfos(request);
        List<CartInfo> infoList = cartInfoDtoList.stream().map(cartInfoDto -> {
            CartInfo cartInfo = new CartInfo();
            BeanUtils.copyProperties(cartInfoDto, cartInfo);
            return cartInfo;
        }).collect(Collectors.toList());
        objectResponse.setResult(infoList);
        objectResponse.setCode("200");
        return  objectResponse;
    }
}

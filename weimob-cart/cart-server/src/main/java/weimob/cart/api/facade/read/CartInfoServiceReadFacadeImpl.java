package weimob.cart.api.facade.read;

import cart.response.Response;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import weimob.cart.api.facade.CartInfoServiceReadFacade;
import weimob.cart.api.response.CartInfo;
import weimob.cart.server.converter.CartInfoConverter;
import weimob.cart.server.domain.dto.CartInfoDto;
import weimob.cart.server.domain.dto.GoodsDto;
import weimob.cart.server.query.CartInfosQuery;
import weimob.cart.server.service.CartService;
import weimob.cart.server.service.GoodsService;

import java.util.Collections;
import java.util.List;

/**
 * @Author: 老张
 * @Date: 2020/3/31
 */
@Service
@Component
public class CartInfoServiceReadFacadeImpl implements CartInfoServiceReadFacade {
    @Autowired
    private CartService cartService;
    @Autowired
    private GoodsService goodsService;

    @Override
    public Response<List<CartInfo>> listCartInfos(String userId) {
        if (StringUtils.isEmpty(userId)) {
            return Response.ok(Collections.emptyList());
        }
        CartInfosQuery request = new CartInfosQuery();
        request.setUserId(userId);
        List<CartInfoDto> cartInfoDtoList = cartService.listCartInfo(request);

        if (cartInfoDtoList.isEmpty()) {
            return Response.ok(Collections.emptyList());
        }
        List<GoodsDto> goodsInfos = goodsService.getGoodsInfos();
        List<CartInfo> cartInfoList = CartInfoConverter.cartInfoList(cartInfoDtoList,goodsInfos);
        return Response.ok(cartInfoList);
    }
}

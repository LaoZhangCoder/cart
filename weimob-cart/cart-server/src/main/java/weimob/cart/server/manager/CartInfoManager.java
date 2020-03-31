package weimob.cart.server.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import weimob.cart.server.domain.dto.CartInfoDto;
import weimob.cart.server.query.CartInfoSaveQuery;
import weimob.cart.server.query.CartInfosQuery;
import weimob.cart.server.service.CartService;

import java.util.List;

/**
 * @Author: 老张
 * @Date: 2020/3/31
 */
@Component
public class CartInfoManager {
    @Autowired
    private CartService cartService;

    public Boolean saveCartInfo(List<CartInfoSaveQuery> cartInfoSaveQueries){
        return cartService.insertCartInfo(cartInfoSaveQueries);
    }

    public List<CartInfoDto> listCartInfos(CartInfosQuery cartInfosQuery){
        return cartService.listCartInfo(cartInfosQuery);
    }
}

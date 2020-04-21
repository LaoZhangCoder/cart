package weimob.cart.server.converter;

import weimob.cart.api.request.CartInfosSaveRequest;
import weimob.cart.server.domain.model.CartDo;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: 老张
 * @Date: 2020/4/10
 */
public class CartDoConverter {
    public static List<CartDo> cartDoList(List<CartInfosSaveRequest> cartInfos,String userId) {
        List<CartDo> cartDoList = cartInfos.stream().map(cartInfosSaveRequest -> {
            CartDo cartDo = new CartDo();
            cartDo.setUserId(userId);
            cartDo.setSkuId(cartInfosSaveRequest.getSkuId());
            cartDo.setChecked(cartInfosSaveRequest.getChecked());
            cartDo.setCount(cartInfosSaveRequest.getCount());
            cartDo.setCreateDate(new Date());
            cartDo.setUpdateDate(new Date());
            return cartDo;
        }).collect(Collectors.toList());
        return cartDoList;
    }
}

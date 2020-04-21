package weimob.cart.server.converter;

import org.springframework.beans.BeanUtils;
import weimob.cart.api.response.CartInfo;
import weimob.cart.server.domain.dto.CartInfoDto;
import weimob.cart.server.domain.dto.GoodsDto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: 老张
 * @Date: 2020/4/12
 */
public class CartInfoConverter {

    public static List<CartInfo> cartInfoList(List<CartInfoDto> cartInfoDtoList, List<GoodsDto> goodsInfos){
        List<CartInfo> infoList = cartInfoDtoList.stream().map(cartInfoDto -> {
            CartInfo cartInfo = new CartInfo();
            BeanUtils.copyProperties(cartInfoDto, cartInfo);
            return cartInfo;
        }).collect(Collectors.toList());
        return infoList;
    }
}

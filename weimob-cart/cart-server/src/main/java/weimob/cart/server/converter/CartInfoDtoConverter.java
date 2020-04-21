package weimob.cart.server.converter;

import org.springframework.beans.BeanUtils;
import weimob.cart.server.domain.dto.CartInfoDto;
import weimob.cart.server.domain.model.CartDo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: 老张
 * @Date: 2020/4/12
 */
public class CartInfoDtoConverter {

    public static List<CartInfoDto> cartInfoDtoList(List<CartDo> list) {
        List<CartInfoDto> cartInfoDtoList = list.stream().map(cartDo -> {
            CartInfoDto cartInfoDto = new CartInfoDto();
            BeanUtils.copyProperties(cartDo, cartInfoDto);
            return cartInfoDto;
        }).collect(Collectors.toList());
        return cartInfoDtoList;
    }
}

package web.converter;

import cart.response.Response;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import web.response.CartInfoVo;
import weimob.cart.api.request.CartInfosSaveRequest;
import weimob.cart.api.response.CartInfo;
import weimob.cart.api.response.GoodsInfo;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: 老张
 * @Date: 2020/4/12
 */
@Slf4j
public class CartInfoVoResponseConverter {

    static ObjectMapper objectMapper = new ObjectMapper();

    public static Response<List<CartInfoVo>> listCartInfVo(String cartValue) {
        Response<List<CartInfoVo>> response = new Response<>();
        String decode = null;
        try {
            decode = URLDecoder.decode(cartValue, "utf-8");
            List<CartInfoVo> cartInfoVos = objectMapper.readValue(decode, new TypeReference<List<CartInfoVo>>() {
            });
            response.setResult(cartInfoVos);
        } catch (UnsupportedEncodingException e) {
            log.error("购物车cookie编码错误:{}", cartValue);
        } catch (Exception exception) {
            log.error("购物车数据json转换异常: {}", exception.getCause());

        }
        return response;
    }

    public static Response<List<CartInfoVo>> listCartInfVo(List<CartInfo> cartInfos, List<GoodsInfo> goodsInfoList) {
        List<CartInfoVo> cartInfoVoList = cartInfos.stream().map(cartInfo -> {
            CartInfoVo cartInfoVo = CartInfoVo.builder().build();
            BeanUtils.copyProperties(cartInfo, cartInfoVo);
            goodsInfoList.forEach(goodsInfo -> {
                if (goodsInfo.getId().equals(cartInfoVo.getSkuId())) {
                    cartInfoVo.setGoodsNum(goodsInfo.getGoodsNum());
                    cartInfoVo.setGoodsName(goodsInfo.getGoodsName());
                    cartInfoVo.setGoodsPrice(goodsInfo.getGoodsPrice());
                }
            });
            return cartInfoVo;
        }).collect(Collectors.toList());
        return Response.ok(cartInfoVoList);
    }
}

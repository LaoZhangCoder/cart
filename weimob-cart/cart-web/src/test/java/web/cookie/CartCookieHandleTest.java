package web.cookie;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import web.response.CartInfoVo;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * @Author: 老张
 * @Date: 2020/4/13
 */
public class CartCookieHandleTest {

    @Test
    public void deleteCartById() {
        CartCookieHandle cartCookieHandle = new CartCookieHandle();
        String str = "[{\"id\":null,\"userId\":null,\"skuId\":1,\"count\":4,\"goodsNum\":19,\"checked\":0,\"goodsName\":\"裤子\",\"goodsPrice\":11.1},{\"id\":null,\"userId\":null,\"skuId\":2,\"count\":1,\"goodsNum\":12,\"checked\":1,\"goodsName\":\"袜子\",\"goodsPrice\":100},{\"id\":null,\"userId\":null,\"skuId\":3,\"count\":1,\"goodsNum\":13,\"checked\":1,\"goodsName\":\"电脑\",\"goodsPrice\":1111},{\"id\":null,\"userId\":null,\"skuId\":4,\"count\":1,\"goodsNum\":123,\"checked\":1,\"goodsName\":\"游戏机\",\"goodsPrice\":1233}]";
        ObjectMapper objectMapper = new ObjectMapper();

        try {

            List<CartInfoVo> cartInfoVos = objectMapper.readValue(str, new TypeReference<List<CartInfoVo>>() {
            });
            System.out.println("删除前-------------------");
            cartInfoVos.stream().forEach(System.out::print);
            System.out.println("删除后-------------------");
            List<Integer> collect = Arrays.stream(new Integer[]{1, 2}).collect(Collectors.toList());
            String s = cartCookieHandle.deleteCartByIds(cartInfoVos, collect);
            List<CartInfoVo> cartInfoVos2 = objectMapper.readValue(s, new TypeReference<List<CartInfoVo>>() {
            });
            cartInfoVos2.stream().forEach(System.out::print);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
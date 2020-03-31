package weimob.cart.server.service.impl;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weimob.cart.server.dao.CartDao;
import weimob.cart.server.domain.dto.CartInfoDto;
import weimob.cart.server.domain.model.CartDo;
import weimob.cart.server.query.CartInfoSaveQuery;
import weimob.cart.server.query.CartInfosQuery;
import weimob.cart.server.service.CartService;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: 老张
 * @Date: 2020/3/31
 */
@Slf4j
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartDao cartDao;

    @Override
    public Boolean insertCartInfo(List<CartInfoSaveQuery> cartInfoSaveQuery) {
        List<CartDo> cartDoList = cartInfoSaveQuery.stream().map(cartInfo -> {
            CartDo cartDo = new CartDo();
            cartDo.setUserId(cartInfo.getUserId());
            cartDo.setSkuId(cartInfo.getSkuId());
            cartDo.setChecked(1);
            cartDo.setCount(cartInfo.getCount());
            cartDo.setCreateDate(new Date());
            cartDo.setUpdateDate(new Date());
            return cartDo;
        }).collect(Collectors.toList());
        //先删除所有购物车在重新添加
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("userId", cartInfoSaveQuery.get(0).getUserId());
        cartDao.deletesByCondition(map);
        Integer creates = cartDao.creates(cartDoList);
        return creates >= 0;
    }

    @Override
    public List<CartInfoDto> listCartInfo(CartInfosQuery cartInfosQuery) {

        HashMap<String, Object> map = Maps.newHashMap();
        map.put("userId", cartInfosQuery.getUserId());
        List<CartDo> list = cartDao.list(map);
        List<CartInfoDto> cartInfoDtoList = list.stream().map(cartDo -> {
            CartInfoDto cartInfoDto = new CartInfoDto();
            BeanUtils.copyProperties(cartDo, cartInfoDto);
            return cartInfoDto;
        }).collect(Collectors.toList());
        return cartInfoDtoList;
    }
}

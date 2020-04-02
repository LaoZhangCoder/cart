package weimob.cart.server.service.impl;

import cart.response.Response;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weimob.cart.api.request.CartInfoUpdateRequest;
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

    @Override
    public Response<String> updateCartInfo(CartInfoUpdateRequest request) {
        Response<String> response = new Response<>();
        if (request.getIsChecked()) {
            HashMap<String, Object> map = Maps.newHashMap();
            if (!request.getIsAllChecked()) {
                map.put("userId", request.getUserId());
                map.put("skuId", request.getSkuId());
                CartDo cartDo = cartDao.findByUniqueIndex(map);
                if (cartDo == null) {
                    return response;
                }
                cartDo.setChecked(cartDo.getChecked() == 0 ? 1 : 0);
                Boolean update = cartDao.update(cartDo);
                response.setResult(update.toString());
                return response;
            }
            map.clear();
            map.put("userId", request.getUserId());
            List<CartDo> list = cartDao.list(map);
            if (list.isEmpty()) {
                return response;
            }
            //用户购物车当中是否存在未挑选的
            Boolean excludeUnSelected = isExcludeUnSelected(list);
            if (excludeUnSelected) {
                //将所有购物车挑选状态更新为true
                list.stream().forEach(cartDo -> {
                    cartDo.setChecked(1);
                    cartDao.update(cartDo);
                });
                response.setResult("true");
                return response;
            }
            //将所有购物车挑选状态更新为false
            list.stream().forEach(cartDo -> {
                cartDo.setChecked(0);
                cartDao.update(cartDo);
            });
            response.setResult("true");
            return response;
        }
        return response;
    }

    private Boolean isExcludeUnSelected(List<CartDo> list) {
        boolean isExcludeUnChecked = false;
        for (CartDo cartDo : list) {
            if (cartDo.getChecked().equals(0)) {
                isExcludeUnChecked = true;
            }
        }
        return isExcludeUnChecked;
    }
}

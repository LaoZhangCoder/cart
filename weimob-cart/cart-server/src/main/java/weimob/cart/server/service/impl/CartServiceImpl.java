package weimob.cart.server.service.impl;

import cart.enums.MessageEnum;
import cart.exception.ServiceException;
import cart.response.Response;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import weimob.cart.api.request.CartInfoUpdateRequest;
import weimob.cart.api.request.CartInfosSaveRequest;
import weimob.cart.api.request.MergeCartInfoRequest;
import weimob.cart.api.response.CartInfo;
import weimob.cart.server.converter.CartDoConverter;
import weimob.cart.server.converter.CartInfoDtoConverter;
import weimob.cart.server.dao.CartDao;
import weimob.cart.server.dao.GoodsDao;
import weimob.cart.server.domain.dto.CartInfoDto;
import weimob.cart.server.domain.model.CartDo;
import weimob.cart.server.domain.model.GoodsDo;
import weimob.cart.server.manager.CartInfoManager;
import weimob.cart.server.query.CartInfoSaveQuery;
import weimob.cart.server.query.CartInfosQuery;
import weimob.cart.server.service.CartService;

import java.util.*;
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

    @Autowired
    private CartInfoManager cartInfoManager;

    @Autowired
    private GoodsDao goodsDao;

    @Override
    public Boolean insertCartInfo(List<CartInfoSaveQuery> cartInfoSaveQuery) {
        List<CartDo> cartDoList = cartInfoSaveQuery.stream().map(cartInfo -> {
            CartDo cartDo = new CartDo();
            cartDo.setUserId(cartInfo.getUserId());
            cartDo.setSkuId(cartInfo.getSkuId());
            cartDo.setChecked(cartInfo.getChecked());
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
        if (list.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        return CartInfoDtoConverter.cartInfoDtoList(list);
    }

    @Override
    public Boolean mergeCartInfo(MergeCartInfoRequest request) {
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("userId", request.getUserId());
        List<CartDo> cartDo = cartDao.list(map);
        if (!cartDo.isEmpty()) {
            sameCartNumIncrease(cartDo, request.getCartInfos(), request.getUserId());
        }
        notSameCartAddToList(request.getCartInfos(), cartDo, request.getUserId());
        //先删除所有购物车在重新添加需要事务支持
        return cartInfoManager.addAndDeleteTrans(map, request);
    }

    private void notSameCartAddToList(List<CartInfosSaveRequest> cartInfosSaveRequests, List<CartDo> cartDoList, String userId) {
        Set<Integer> cartIds = cartInfosSaveRequests.stream().map(CartInfosSaveRequest::getSkuId).collect(Collectors.toSet());
        for (CartDo cartDo : cartDoList) {
            if (!cartIds.contains(cartDo.getSkuId())) {
                CartInfosSaveRequest saveRequest = new CartInfosSaveRequest();
                saveRequest.setSkuId(cartDo.getSkuId());
                saveRequest.setUserId(userId);
                saveRequest.setChecked(cartDo.getChecked());
                saveRequest.setCount(cartDo.getCount());
                cartInfosSaveRequests.add(saveRequest);
            }
        }
    }

    private void sameCartNumIncrease(List<CartDo> cartDoList, List<CartInfosSaveRequest> cartList, String userId) {
        for (CartInfosSaveRequest cartInfo : cartList) {
            for (CartDo cartInfoVo : cartDoList) {
                if (cartInfoVo.getSkuId().equals(cartInfo.getSkuId())) {
                    if (!isOverMaxGoodNum(cartInfo, cartInfoVo)) {
                        cartInfo.setCount(cartInfo.getCount() + cartInfoVo.getCount());
                    }
                }
            }
            cartInfo.setUserId(userId);
        }
    }

    private Boolean isOverMaxGoodNum(CartInfosSaveRequest cartInfo, CartDo cartInfoVo) {
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("id", cartInfoVo.getSkuId());
        GoodsDo goodsDo = goodsDao.findByUniqueIndex(map);
        if (cartInfo.getCount() + cartInfoVo.getCount() > goodsDo.getGoodsNum()) {
            cartInfo.setCount(cartInfo.getCount());
            return true;
        }
        return false;
    }

    @Override
    public String updateCartInfo(CartInfoUpdateRequest request) {
        HashMap<String, Object> map = Maps.newHashMap();
        if (request.getIsChecked()) {
            if (!request.getIsAllChecked()) {
                CartDo cartDo = getCartDo(request, map);
                if (cartDo == null) {
                    throw new ServiceException("update cart not include");
                }
                cartDo.setChecked(cartDo.getChecked() == 0 ? 1 : 0);
                Boolean update = cartDao.update(cartDo);
                if (update) {
                    return MessageEnum.OPERATION_SUCCESS.getReasonPhrase();
                }
                return MessageEnum.OPERATION_FAIL.getReasonPhrase();
            }
            map.clear();
            map.put("userId", request.getUserId());
            List<CartDo> list = cartDao.list(map);
            if (list.isEmpty()) {
                return MessageEnum.OPERATION_FAIL.getReasonPhrase();
            }
            //用户购物车当中是否存在未挑选的
            Boolean excludeUnSelected = isExcludeUnSelected(list);
            if (excludeUnSelected) {
                //将所有购物车挑选状态更新为true
                list.stream().forEach(cartDo -> {
                    cartDo.setChecked(1);
                    cartDao.update(cartDo);
                });
                return MessageEnum.OPERATION_SUCCESS.getReasonPhrase();
            }
            //将所有购物车挑选状态更新为false
            list.stream().forEach(cartDo -> {
                cartDo.setChecked(0);
                cartDao.update(cartDo);
            });
            return MessageEnum.OPERATION_SUCCESS.getReasonPhrase();
        }
        //是否是修改商品数量
        if (request.getIsCount()) {
            map.clear();
            CartDo cartDo = getCartDo(request, map);
            if (cartDo == null) {
                throw new ServiceException("update cart not include");
            }
            map.clear();
            map.put("id", request.getSkuId());
            GoodsDo goodsDo = goodsDao.findByUniqueIndex(map);
            if (request.getCount() > goodsDo.getGoodsNum()) {
                throw new ServiceException("购物的数量超过了商品数量!");
            }
            //修改后的数量
            cartDo.setCount(request.getCount());
            Boolean update = cartDao.update(cartDo);
            if (update) {
                return MessageEnum.OPERATION_SUCCESS.getReasonPhrase();
            }
            return MessageEnum.OPERATION_FAIL.getReasonPhrase();

        }
        return MessageEnum.OPERATION_FAIL.getReasonPhrase();
    }

    @Override
    public Response<String> deleteCart(Integer id) {
        Response<String> response = new Response<>();
        Boolean delete = cartDao.delete(Long.valueOf(id));
        if (delete) {
            response.setResult("删除成功");
        }
        response.setError("删除失败");
        return response;
    }

    private CartDo getCartDo(CartInfoUpdateRequest request, HashMap<String, Object> map) {
        map.put("userId", request.getUserId());
        map.put("skuId", request.getSkuId());
        return cartDao.findByUniqueIndex(map);
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

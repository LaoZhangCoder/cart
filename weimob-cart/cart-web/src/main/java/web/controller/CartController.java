package web.controller;

import cart.enums.MessageEnum;
import cart.response.Response;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.constants.RedisConstant;
import web.converter.*;
import web.cookie.CartCookieHandle;
import web.query.CartAddQuery;
import web.query.CartDeleteQuery;
import web.query.CartInfoQuery;
import web.query.CartUpdateQuery;
import web.response.CartInfoVo;
import web.utils.CookieUtils;
import web.utils.RedisClientUtils;
import weimob.cart.api.facade.CartInfoServiceReadFacade;
import weimob.cart.api.facade.CartInfoServiceWriteFacade;
import weimob.cart.api.facade.GoodsServiceReadFacade;
import weimob.cart.api.request.CartInfoDeleteRequest;
import weimob.cart.api.request.CartInfoListRemoveRequest;
import weimob.cart.api.request.CartInfoUpdateRequest;
import weimob.cart.api.request.MergeCartInfoRequest;
import weimob.cart.api.response.CartInfo;
import weimob.cart.api.response.GoodsInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * @Author: 老张
 * @Date: 2020/3/29
 */
@RestController
@RequestMapping(value = "/api/cart/")
public class CartController {

    @Autowired
    private CartCookieHandle cartCookieHandle;

    @Reference
    private CartInfoServiceWriteFacade cartInfoServiceWriteFacade;

    @Reference
    private CartInfoServiceReadFacade cartInfoServiceReadFacade;

    @Reference
    private GoodsServiceReadFacade goodsServiceReadFacade;

    @Autowired
    private RedisClientUtils redisClientUtils;

    @PostMapping(value = "add")
    public Response<String> addGoods(HttpServletRequest request, HttpServletResponse response, @RequestBody CartAddQuery cartAddQuery) {
        if (cartAddQuery.getLogin()) {
            redisClientUtils.deleteCache(RedisConstant.KEY, cartAddQuery.getUserId());
            MergeCartInfoRequest cartInfoRequest = MergeCartInfoRequestConverter.mergeCartInfoRequest(cartAddQuery);
            boolean isSuccess = cartInfoServiceWriteFacade.mergeCartInfo(cartInfoRequest).getResult();
            if (isSuccess) {
                return Response.ok(MessageEnum.OPERATION_SUCCESS.getReasonPhrase());
            }
            return Response.fail(MessageEnum.OPERATION_FAIL.getReasonPhrase());
        }
        //未登录添加购物车到本地Cookie
        String cookieJsonValue = cartCookieHandle.addCartToCookie(cartAddQuery, request.getCookies(), cartAddQuery.getCookieCart());
        if (cookieJsonValue != null) {
            CookieUtils.addCookie(response, cookieJsonValue);
            return Response.ok(MessageEnum.OPERATION_SUCCESS.getReasonPhrase());
        }
        return Response.fail(MessageEnum.OPERATION_FAIL.getReasonPhrase());
    }

    @GetMapping(value = "list")
    public Response<List<CartInfoVo>> listCarts(HttpServletRequest request, CartInfoQuery cartInfoQuery) {
        //判断用户是否登录，未登录则从本地cookie获取购物车数据
        if (cartInfoQuery.getLogin()) {
            List<CartInfo> result;
            if ((result = redisClientUtils.getCache(RedisConstant.KEY, cartInfoQuery.getUserId())) == null) {
                result = cartInfoServiceReadFacade.listCartInfos(CookieUtils.getUserIdByCookie(request.getCookies())).getResult();
            }
            if (result.isEmpty()) {
                return Response.ok(null);
            } else {
                redisClientUtils.save(RedisConstant.KEY, cartInfoQuery.getUserId(), result, RedisConstant.TTL);
            }
            List<GoodsInfo> goodsInfos = goodsServiceReadFacade.getGoodsInfos().getResult();
            Response<List<CartInfoVo>> listResponse = CartInfoVoResponseConverter.listCartInfVo(result, goodsInfos);
            return listResponse;
        }
        if (!cartInfoQuery.getCookieCart()) {
            return Response.ok(null);
        }
        return CartInfoVoResponseConverter.listCartInfVo(CookieUtils.getCartCookieValue(request.getCookies()));
    }

    @PatchMapping(value = "updateCartInfo")
    public Response<String> updateCartInfo(HttpServletRequest request, @RequestBody CartUpdateQuery cartUpdateQuery, HttpServletResponse response) {
        if (cartUpdateQuery.getLogin()) {
            redisClientUtils.deleteCache(RedisConstant.KEY, cartUpdateQuery.getUserId());
            CartInfoUpdateRequest cartInfoUpdateRequest = CartInfoUpdateRequestConverter.getCartInfoUpdateRequest(request, cartUpdateQuery);
            return cartInfoServiceWriteFacade.updateCartInfo(cartInfoUpdateRequest);
        }
        if (!cartUpdateQuery.getCookieCart()) {
            return Response.fail(MessageEnum.OPERATION_FAIL.getReasonPhrase());
        }
        //没登录直接更新本地cookie购物车
        List<CartInfoVo> result = cartCookieHandle.getCookieJsonToObject(CookieUtils.getCartCookieValue(request.getCookies()));
        String updateCookieValue = cartCookieHandle.updateCookieValue(result, cartUpdateQuery);
        CookieUtils.addCookie(response, updateCookieValue);
        return Response.ok(MessageEnum.OPERATION_SUCCESS.getReasonPhrase());
    }

    @DeleteMapping(value = "deleteCart/{id}")
    public Response<String> deleteCart(@PathVariable(name = "id") Integer id, HttpServletRequest request, HttpServletResponse response) {
        boolean isLogin = CookieUtils.isLogin(request.getCookies());
        boolean isExistCart = CookieUtils.isExcludeCookieCart(request.getCookies());
        if (isLogin) {
            String userId = CookieUtils.getUserIdByCookie(request.getCookies());
            redisClientUtils.deleteCache(RedisConstant.KEY, userId);
            CartInfoDeleteRequest cartInfoDeleteRequest = CartInfoDeleteRequestConverter.cartInfoDeleteRequest(userId, id);
            Response<String> stringResponse = cartInfoServiceWriteFacade.deleteCart(cartInfoDeleteRequest);
            if (stringResponse.isSuccess()) {
                return Response.ok(stringResponse.getResult());
            }
            return Response.fail(stringResponse.getError());
        }
        if (!isExistCart) {
            return Response.fail(MessageEnum.OPERATION_FAIL.getReasonPhrase());
        }
        //删除本地cookie cart
        List<CartInfoVo> cartInfoVoList = cartCookieHandle.getCookieJsonToObject(CookieUtils.getCartCookieValue(request.getCookies()));

        String cookieValue = cartCookieHandle.deleteCartById(cartInfoVoList, id);

        CookieUtils.addCookie(response, cookieValue);
        return Response.ok(MessageEnum.OPERATION_SUCCESS.getReasonPhrase());
    }

    @PostMapping(value = "allSelect")
    public Response<String> deleteSelectCart(HttpServletResponse response, HttpServletRequest request, @RequestBody CartDeleteQuery skuIds) {
        if (skuIds.getLogin()) {
            String userId = CookieUtils.getUserIdByCookie(request.getCookies());
            redisClientUtils.deleteCache(RedisConstant.KEY, userId);
            CartInfoListRemoveRequest cartInfoListRemoveRequest = CartInfoListRemoveRequestConverter.cartInfoListRemoveRequest(userId, skuIds.getIds());
            Response<String> stringResponse = cartInfoServiceWriteFacade.removeCartList(cartInfoListRemoveRequest);
            if (stringResponse.isSuccess()) {
                return Response.ok(stringResponse.getResult());
            }
            return Response.fail(stringResponse.getError());
        }
        if (!skuIds.getCookieCart()) {
            return Response.ok(MessageEnum.OPERATION_FAIL.getReasonPhrase());
        }

        //删除本地cookie cart
        List<CartInfoVo> cartInfoVoList = cartCookieHandle.getCookieJsonToObject(CookieUtils.getCartCookieValue(request.getCookies()));
        String cookieValue = cartCookieHandle.deleteCartByIds(cartInfoVoList, skuIds.getIds());
        CookieUtils.addCookie(response, cookieValue);
        return Response.ok(MessageEnum.OPERATION_SUCCESS.getReasonPhrase());
    }

}

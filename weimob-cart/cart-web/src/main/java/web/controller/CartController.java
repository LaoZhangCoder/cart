package web.controller;

import cart.enums.MessageEnum;
import cart.response.Response;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.converter.MergeCartInfoRequestConverter;
import web.cookie.CartCookieHandle;
import web.manager.CartManager;
import web.query.CartAddQuery;
import web.query.CartUpdateQuery;
import web.response.CartInfoVo;
import web.utils.CookieUtils;
import weimob.cart.api.facade.CartInfoServiceWriteFacade;
import weimob.cart.api.request.CartInfosSaveRequest;
import weimob.cart.api.request.MergeCartInfoRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


/**
 * @Author: 老张
 * @Date: 2020/3/29
 */
@RestController
@RequestMapping(value = "/api/cart/")
public class CartController {
    @Autowired
    private CartManager cartManager;

    @Autowired
    private CartCookieHandle cartCookieHandle;

    @Reference
    private CartInfoServiceWriteFacade cartInfoServiceWriteFacade;

    @PostMapping(value = "add")
    public Response<String> addGoods(HttpServletRequest request, HttpServletResponse response, @RequestBody CartAddQuery cartAddQuery) {
        boolean isLogin = CookieUtils.isLogin(request.getCookies());
        boolean excludeCookieCart = CookieUtils.isExcludeCookieCart(request.getCookies());
        if (isLogin) {
            MergeCartInfoRequest cartInfoRequest = MergeCartInfoRequestConverter.mergeCartInfoRequest(request.getCookies(), cartAddQuery);
            boolean isSuccess = cartInfoServiceWriteFacade.mergeCartInfo(cartInfoRequest).getResult();
            if (isSuccess) {
                return Response.ok(MessageEnum.OPERATION_SUCCESS.getReasonPhrase());
            }
            return Response.fail(MessageEnum.OPERATION_FAIL.getReasonPhrase());
        }
        //未登录添加购物车到本地Cookie
        String cookieJsonValue = cartCookieHandle.addCartToCookie(cartAddQuery, request.getCookies(), excludeCookieCart);
        if (cookieJsonValue != null) {
            CookieUtils.addCookie(response, cookieJsonValue);
            return Response.ok(MessageEnum.OPERATION_SUCCESS.getReasonPhrase());
        }
        return Response.fail(MessageEnum.OPERATION_FAIL.getReasonPhrase());
    }

    @GetMapping(value = "list")
    public Response<List<CartInfoVo>> listCarts(HttpServletRequest request) {
        return cartManager.listCarts(request);
    }

    @PatchMapping(value = "updateCartInfo")
    public Response<String> updateCartInfo(HttpServletRequest request, @RequestBody CartUpdateQuery cartUpdateQuery, HttpServletResponse response) {
        return cartManager.updateCartChecked(request, cartUpdateQuery, response);
    }

    @DeleteMapping(value = "deleteCart/{id}")
    public Response<String> deleteCart(@PathVariable(name = "id") Integer id) {
        return cartManager.deleteCart(id);
    }
}

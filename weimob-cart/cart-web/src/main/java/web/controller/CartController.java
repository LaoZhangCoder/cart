package web.controller;

import cart.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.manager.CartManager;
import web.query.CartAddQuery;
import web.query.CartUpdateQuery;
import web.response.CartInfoVo;
import weimob.cart.api.request.CartInfoUpdateRequest;

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
    private CartManager cartManager;

    @PostMapping(value = "add")
    public Response<String> addGoods(HttpServletRequest request, HttpServletResponse response, @RequestBody CartAddQuery cartAddQuery) {
        try {
            return cartManager.addGoods(request, response, cartAddQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping(value = "list")
    public Response<List<CartInfoVo>> listCarts(HttpServletRequest request) {
        return cartManager.listCarts(request);
    }

    @PatchMapping(value = "updateCartInfo")
    public Response<String> updateCartInfo(HttpServletRequest request, @RequestBody CartUpdateQuery cartUpdateQuery,HttpServletResponse response) {
        return cartManager.updateCartChecked(request, cartUpdateQuery,response);
    }

    @DeleteMapping(value = "deleteCart/{id}")
    public Response<String> deleteCart(@PathVariable(name = "id") Integer id){
        return cartManager.deleteCart(id);
    }
}

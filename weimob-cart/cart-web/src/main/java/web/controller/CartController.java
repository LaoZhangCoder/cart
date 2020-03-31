package web.controller;

import cart.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.manager.CartManager;
import web.query.CartAddQuery;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


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
            return cartManager.addGoods(request,response,cartAddQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

package web.controller;

import cart.response.Response;
import web.manager.GoodsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.response.GoodsVo;
import java.util.List;

/**
 * @Author: 老张
 * @Date: 2020/3/26
 */
@RestController
@RequestMapping(value = "/api/goods/")
public class GoodsController {

    @Autowired
    private GoodsManager goodsManager;

    @GetMapping(value = "list")
    public Response<List<GoodsVo>> getGoodsList(){
        return goodsManager.getGoodsInfo();
    }
}




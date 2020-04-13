package web.controller;

import cart.response.Response;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.response.GoodsVo;
import weimob.cart.api.facade.GoodsServiceReadFacade;
import weimob.cart.api.response.GoodsInfo;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: 老张
 * @Date: 2020/3/26
 */
@RestController
@RequestMapping(value = "/api/goods/")
public class GoodsController {
    @Reference
    private GoodsServiceReadFacade goodsServiceReadFacade;

    @GetMapping(value = "list")
    public Response<List<GoodsVo>> getGoodsList() {
        List<GoodsInfo> result = goodsServiceReadFacade.getGoodsInfos().getResult();
        if (result == null || result.isEmpty()) {
            return Response.ok(Collections.emptyList());
        }
        List<GoodsVo> goodsVoList = getGoodsVos(result);
        return Response.ok(goodsVoList);
    }

    private List<GoodsVo> getGoodsVos(List<GoodsInfo> result) {
        return result.stream().map(goodsInfo -> {
            GoodsVo goodsVo = new GoodsVo();
            BeanUtils.copyProperties(goodsInfo, goodsVo);
            return goodsVo;
        }).collect(Collectors.toList());
    }
}




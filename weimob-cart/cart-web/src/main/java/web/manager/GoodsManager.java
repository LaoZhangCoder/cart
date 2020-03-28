package web.manager;

import cart.response.Response;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import web.response.GoodsVo;
import weimob.cart.api.facade.GoodsServiceReadFacade;
import weimob.cart.api.response.GoodsInfo;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: 老张
 * @Date: 2020/3/27
 */
@Component
public class GoodsManager {
    @Reference
    private GoodsServiceReadFacade goodsServiceReadFacade;

    public Response<List<GoodsVo>> getGoodsInfo() {
        Response<List<GoodsVo>> response = new Response<>();
        List<GoodsInfo> goodsInfos = goodsServiceReadFacade.getGoodsInfos();
        if (goodsInfos.isEmpty()) {
            response.setResult(null);
        }
        List<GoodsVo> goodsVos = goodsInfos.stream().map(goodsInfo -> {
            GoodsVo goodsVo = new GoodsVo();
            BeanUtils.copyProperties(goodsInfo, goodsVo);
            return goodsVo;
        }).collect(Collectors.toList());
        response.setResult(goodsVos);
        return response;
    }
}

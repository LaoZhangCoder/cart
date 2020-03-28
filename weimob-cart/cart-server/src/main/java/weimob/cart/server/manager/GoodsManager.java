package weimob.cart.server.manager;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import weimob.cart.api.response.GoodsInfo;
import weimob.cart.server.domain.dto.GoodsDto;
import weimob.cart.server.service.GoodsService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: 老张
 * @Date: 2020/3/27
 */
@Component
public class GoodsManager {

    @Autowired
    private GoodsService goodsService;

    public List<GoodsInfo> getGoods() {
        List<GoodsInfo> goodsInfoList = new ArrayList<>();
        List<GoodsDto> goodsInfos = goodsService.getGoodsInfos();
        if (goodsInfos.isEmpty()) {
            return goodsInfoList;
        }
        goodsInfoList = goodsInfos.stream().map(goodsDto -> {
            GoodsInfo goodsInfo = new GoodsInfo();
            BeanUtils.copyProperties(goodsDto, goodsInfo);
            return  goodsInfo;
        }).collect(Collectors.toList());
        return  goodsInfoList;
    }
}

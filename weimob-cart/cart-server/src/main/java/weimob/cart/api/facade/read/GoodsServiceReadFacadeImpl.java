package weimob.cart.api.facade.read;

import cart.response.Response;
import com.alibaba.dubbo.config.annotation.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import weimob.cart.api.facade.GoodsServiceReadFacade;
import weimob.cart.api.response.GoodsInfo;
import weimob.cart.server.domain.dto.GoodsDto;
import weimob.cart.server.service.GoodsService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: 老张
 * @Date: 2020/3/27
 */

@Slf4j
@Service
@Component
public class GoodsServiceReadFacadeImpl implements GoodsServiceReadFacade {

    @Autowired
    private GoodsService goodsService;

    @Override
    public Response<List<GoodsInfo>> getGoodsInfos() {
        List<GoodsDto> goodsInfos = goodsService.getGoodsInfos();
        return Response.ok(goodsInfoListConverter(goodsInfos));
    }

    private List<GoodsInfo> goodsInfoListConverter(List<GoodsDto> goodsInfos) {
        List<GoodsInfo> collect = goodsInfos.stream().map(goodsDto -> {
            GoodsInfo goodsInfo = new GoodsInfo();
            BeanUtils.copyProperties(goodsDto, goodsInfo);
            return goodsInfo;
        }).collect(Collectors.toList());
        return collect;
    }

}

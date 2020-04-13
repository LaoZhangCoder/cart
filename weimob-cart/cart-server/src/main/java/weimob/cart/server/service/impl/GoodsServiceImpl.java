package weimob.cart.server.service.impl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weimob.cart.server.dao.GoodsDao;
import weimob.cart.server.domain.dto.GoodsDto;
import weimob.cart.server.domain.model.GoodsDo;
import weimob.cart.server.service.GoodsService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: 老张
 * @Date: 2020/3/26
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsDao goodsDao;

    @Override
    public List<GoodsDto> getGoodsInfos() {
        List<GoodsDto> goodsDtoList = new ArrayList();
        List<GoodsDo> goodsDos = goodsDao.listAll();
        if (goodsDos.isEmpty()) {
            return goodsDtoList;
        }
        return goodsDtoListConverter(goodsDos);
    }

    private List<GoodsDto> goodsDtoListConverter(List<GoodsDo> goodsDos) {
        List<GoodsDto> goodsDtoList;
        goodsDtoList = goodsDos.stream().map(goodsDo -> {
            GoodsDto goodsDto = new GoodsDto();
            BeanUtils.copyProperties(goodsDo, goodsDto);
            return goodsDto;
        }).collect(Collectors.toList());
        return goodsDtoList;
    }
}

package weimob.cart.server.service;

import weimob.cart.server.domain.dto.GoodsDto;

import java.util.List;

/**
 * @Author: 老张
 * @Date: 2020/3/26
 */
public interface GoodsService {
    /**
     * @description 这个接口是返回所有商品列表
     * @param  : not param
     * @return 商品列表
     * @exception not include exception
     */
    List<GoodsDto> getGoodsInfos();
}

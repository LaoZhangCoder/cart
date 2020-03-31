package weimob.cart.api.facade;

import weimob.cart.api.response.GoodsInfo;

import java.util.List;

/**
 * @Author: 老张
 * @Date: 2020/3/27
 */
public interface GoodsServiceReadFacade {
    /**
     * @Author: laozhang
     * @param
     * @return return goods list
     * @exception
     */
     List<GoodsInfo> getGoodsInfos();


}

package weimob.cart.api.facade.read;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;
import weimob.cart.api.facade.GoodsServiceReadFacade;
import weimob.cart.api.response.GoodsInfo;
import weimob.cart.server.manager.GoodsManager;

import java.util.List;

/**
 * @Author: 老张
 * @Date: 2020/3/27
 */

@Slf4j
@Service
@Component
public class GoodsServiceReadFacadeImpl implements GoodsServiceReadFacade {

    @Autowired
    private GoodsManager goodsManager;

    @Override
    public List<GoodsInfo> getGoodsInfos() {
        return goodsManager.getGoods();
    }

}

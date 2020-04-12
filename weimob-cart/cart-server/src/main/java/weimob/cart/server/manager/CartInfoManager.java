package weimob.cart.server.manager;

import cart.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import weimob.cart.api.request.CartInfoUpdateRequest;
import weimob.cart.api.request.MergeCartInfoRequest;
import weimob.cart.server.converter.CartDoConverter;
import weimob.cart.server.dao.CartDao;
import weimob.cart.server.domain.dto.CartInfoDto;
import weimob.cart.server.domain.model.CartDo;
import weimob.cart.server.query.CartInfoSaveQuery;
import weimob.cart.server.query.CartInfosQuery;
import weimob.cart.server.service.CartService;

import java.util.HashMap;
import java.util.List;

/**
 * @Author: 老张
 * @Date: 2020/3/31
 */
@Component
public class CartInfoManager {
    @Autowired
    private CartService cartService;

    @Autowired
    private CartDao cartDao;

    @Transactional(rollbackFor = Exception.class)
    public Boolean addAndDeleteTrans(HashMap<String, Object> map, MergeCartInfoRequest request) {
        //先删除所有购物车在重新添加
        try {
            cartDao.deletesByCondition(map);
            List<CartDo> cartDos = CartDoConverter.cartDoList(request.getCartInfos(), request.getUserId());
            Integer creates = cartDao.creates(cartDos);
            if (!creates.equals(cartDos.size())) {
                throw new Exception("事务回滚");
            }
            return true;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }

    }

    public Boolean saveCartInfo(List<CartInfoSaveQuery> cartInfoSaveQueries) {
        return cartService.insertCartInfo(cartInfoSaveQueries);
    }

    public List<CartInfoDto> listCartInfos(CartInfosQuery cartInfosQuery) {
        return cartService.listCartInfo(cartInfosQuery);
    }

    public Response<String> updateCartInfo(CartInfoUpdateRequest request) {
        return  null;
    }

    public Response<String> deleteCart(Integer cartId) {
        return cartService.deleteCart(cartId);
    }
}

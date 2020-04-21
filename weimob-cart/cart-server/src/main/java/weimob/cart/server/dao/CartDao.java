package weimob.cart.server.dao;

import org.springframework.stereotype.Repository;
import weimob.cart.server.common.AbstractMybatisDao;
import weimob.cart.server.domain.model.CartDo;

import java.util.List;
import java.util.Map;

/**
 * @Author: 老张
 * @Date: 2020/3/31
 */
@Repository
public class CartDao extends AbstractMybatisDao<CartDo> {

    public Integer deletesByCondition(Map<?, ?> criteria) {
        return this.sqlSession.delete(this.sqlId("deletesBycondition"),criteria);
    }
}

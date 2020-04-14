package web.query;

import cart.request.AbstractRequest;
import cart.untils.ParamUtil;
import lombok.Data;

import java.util.List;

/**
 * @Author: 老张
 * @Date: 2020/4/14
 */
@Data
public class CartDeleteQuery extends AbstractQuery {
    private List<Integer> ids;

    @Override
    public void checkParam() {
        ParamUtil.notEmpty(ids,"删除列表为空!");
    }
}

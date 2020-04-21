package weimob.cart.api.request;

import cart.request.AbstractRequest;
import cart.untils.ParamUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: 老张
 * @Date: 2020/4/14
 */
@Data
public class CartInfoListRemoveRequest extends AbstractRequest implements Serializable {
    private static final long serialVersionUID = 4725521047489436793L;
    /**
     * 商品ids
     */
    private List<Integer> skuIds;

    /**
     * 用户id
     */
    private String userId;

    @Override
    public void checkParam() {
        ParamUtil.notEmpty(skuIds, "商品id集合不能为空");
        ParamUtil.isBlank(userId, "用户id不能为空");
    }
}

package weimob.cart.api.request;

import cart.request.AbstractRequest;
import cart.untils.ParamUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @Author: 老张
 * @Date: 2020/4/13
 */
@Slf4j
@Data
public class CartInfoDeleteRequest extends AbstractRequest implements Serializable {
    private static final long serialVersionUID = 6601163989720410868L;
    /**
     * 商品id
     */
    private Integer skuId;

    /**
     * 用户id
     */
    private String userId;
    @Override
    public void checkParam() {
        ParamUtil.nonNull(skuId,"商品id不能为空");
        ParamUtil.isBlank(userId,"用户id不能为空");
    }
}

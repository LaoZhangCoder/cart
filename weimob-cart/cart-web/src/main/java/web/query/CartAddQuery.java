package web.query;

import cart.request.AbstractRequest;
import cart.untils.ParamUtil;
import lombok.Data;

/**
 * @Author: 老张
 * @Date: 2020/3/29
 */
@Data
public class CartAddQuery extends AbstractRequest {
    /**
     * 商品id
     */
    private Integer id;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品价格
     */
    private Integer goodsPrice;

    /**
     * 商品数量
     */
    private Integer goodsNum;

    @Override
    public void checkParam() {
        ParamUtil.nonNull(id,"商品id不能为空!");
    }
}

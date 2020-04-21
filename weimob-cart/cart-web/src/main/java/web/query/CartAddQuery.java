package web.query;

import cart.untils.ParamUtil;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: 老张
 * @Date: 2020/3/29
 */
@Data
public class CartAddQuery extends AbstractQuery  {
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
    private BigDecimal goodsPrice;

    /**
     * 商品数量
     */
    private Integer goodsNum;

    @Override
    public void checkParam() {
        ParamUtil.nonNull(id,"商品id不能为空!");
    }
}

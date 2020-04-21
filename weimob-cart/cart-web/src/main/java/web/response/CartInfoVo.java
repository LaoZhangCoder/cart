package web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @Author: 老张
 * @Date: 2020/3/29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartInfoVo {
    /**
     * 购物车id
     */
    private Integer id;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 商品id
     */
    private Integer skuId;
    /**
     * 购买商品数量
     */
    private Integer count;
    /**
     * 商品数量
     */
    private Integer goodsNum;
    /**
     * 勾选状态
     */
    private Integer checked;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品价格
     */
    private BigDecimal goodsPrice;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CartInfoVo that = (CartInfoVo) o;
        return skuId.equals(that.skuId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(skuId);
    }
}

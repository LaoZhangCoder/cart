package weimob.cart.api.response;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: 老张
 * @Date: 2020/3/27
 */
@Data
public class GoodsInfo implements Serializable {

    private static final long serialVersionUID = 2518158374583608243L;
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

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 更新时间
     */

    private Date updateDate;
}

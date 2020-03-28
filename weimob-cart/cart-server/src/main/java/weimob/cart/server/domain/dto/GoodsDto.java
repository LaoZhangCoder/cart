package weimob.cart.server.domain.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: 老张
 * @Date: 2020/3/26
 */
@Data
public class GoodsDto {
    /**
     * id
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

package web.query;

import lombok.Data;

/**
 * @Author: 老张
 * @Date: 2020/3/29
 */
@Data
public class CartAddQuery {
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

}

package weimob.cart.server.query;

import lombok.Data;

/**
 * @Author: 老张
 * @Date: 2020/3/31
 */
@Data
public class CartInfoSaveQuery {
    /**
     * 用户id
     */
    private String userId;

    /**
     * 商品id
     */
    private Integer skuId;

    /**
     * 商品数量
     */
    private Integer count;

    /**
     * 勾选状态
     */
    private Integer checked;
}

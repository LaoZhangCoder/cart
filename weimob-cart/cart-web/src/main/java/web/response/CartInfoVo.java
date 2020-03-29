package web.response;

import lombok.Data;

/**
 * @Author: 老张
 * @Date: 2020/3/29
 */
@Data
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
    private Boolean checked;
}

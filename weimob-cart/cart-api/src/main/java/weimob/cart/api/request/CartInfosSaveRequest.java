package weimob.cart.api.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: 老张
 * @Date: 2020/3/31
 */
@Data
public class CartInfosSaveRequest implements Serializable {
    private static final long serialVersionUID = 435192436111332121L;
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

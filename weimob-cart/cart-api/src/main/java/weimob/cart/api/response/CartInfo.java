package weimob.cart.api.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: 老张
 * @Date: 2020/3/31
 */
@Data
public class CartInfo implements Serializable {
    private static final long serialVersionUID = 2446952748796770897L;
    /**
     * id
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
     * 商品数量
     */
    private Integer count;

    /**
     * 勾选状态
     */
    private Integer checked;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 更新时间
     */
    private Date updateDate;
}

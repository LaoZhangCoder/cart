package weimob.cart.api.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: 老张
 * @Date: 2020/4/21
 */
@Data
public class OrderRequest implements Serializable {
    private static final long serialVersionUID = -6938158230512300250L;
    /**
     * 用户id
     */
    private String userId;
}

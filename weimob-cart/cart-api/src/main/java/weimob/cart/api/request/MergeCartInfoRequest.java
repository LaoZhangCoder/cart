package weimob.cart.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: 老张
 * @Date: 2020/4/10
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MergeCartInfoRequest implements Serializable {
    private static final long serialVersionUID = -4455562513226968249L;
    /**
     * 用户id
     */
    private String userId;

    /**
     * 购物车列表
     */
    List<CartInfosSaveRequest> cartInfos;
}

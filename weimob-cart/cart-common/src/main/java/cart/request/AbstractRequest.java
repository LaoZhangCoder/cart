package cart.request;

/**
 * @Author: 老张
 * @Date: 2020/4/10
 */
public abstract class AbstractRequest {
    /**
     * 入参校验
     * - 校验失败时，统一返回 IllegalArgumentException 异常
     */
    public abstract void checkParam();
}

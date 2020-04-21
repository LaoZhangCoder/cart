package web.query;

/**
 * @Author: 老张
 * @Date: 2020/4/14
 */

public abstract class AbstractQuery {
    protected Boolean isLogin = false;
    protected Boolean isCookieCart = false;
    protected String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getLogin() {
        return isLogin;
    }

    public void setLogin(Boolean login) {
        isLogin = login;
    }

    public Boolean getCookieCart() {
        return isCookieCart;
    }

    public void setCookieCart(Boolean cookieCart) {
        isCookieCart = cookieCart;
    }

    /**
     * 入参校验
     * - 校验失败时，统一返回 IllegalArgumentException 异常
     */
    public abstract void checkParam();
}

package web.response;

import lombok.Data;

/**
 * @Author: 老张
 * @Date: 2020/3/28
 */
@Data
public class UserInfoVo {
    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名称
     */
    private String userName;
}

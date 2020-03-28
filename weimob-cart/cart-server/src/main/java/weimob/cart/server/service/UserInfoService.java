package weimob.cart.server.service;

import weimob.cart.server.domain.dto.UserInfoDto;
import weimob.cart.server.query.UserInfoQuery;

/**
 * @Author: 老张
 * @Date: 2020/3/28
 */
public interface UserInfoService {
    /**
     *
     * @param request
     * @return
     */
    UserInfoDto getUserInfo(UserInfoQuery request);
}

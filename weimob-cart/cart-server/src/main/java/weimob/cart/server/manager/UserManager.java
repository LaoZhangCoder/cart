package weimob.cart.server.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import weimob.cart.server.domain.dto.UserInfoDto;
import weimob.cart.server.query.UserInfoQuery;
import weimob.cart.server.service.UserInfoService;

/**
 * @Author: 老张
 * @Date: 2020/3/28
 */
@Component
public class UserManager {

    @Autowired
    private UserInfoService userInfoService;

    public UserInfoDto getUserInfo(UserInfoQuery query){
        return userInfoService.getUserInfo(query);
    }
}

package weimob.cart.api.facade.read;

import cart.response.Response;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import weimob.cart.api.facade.UserInfoServiceReadFacade;
import weimob.cart.api.request.UserInfoRequest;
import weimob.cart.api.response.UserInfo;
import weimob.cart.server.converter.UserInfoQueryConverter;
import weimob.cart.server.converter.UserInfoResponseConverter;
import weimob.cart.server.domain.dto.UserInfoDto;
import weimob.cart.server.query.UserInfoQuery;
import weimob.cart.server.service.UserInfoService;

/**
 * @Author: 老张
 * @Date: 2020/3/28
 */
@Service
public class UserInfoServiceReadFacadeImpl implements UserInfoServiceReadFacade {

    @Autowired
    private UserInfoService userInfoService;

    @Override
    public Response<UserInfo> getUserInfo(UserInfoRequest request) {
        UserInfoQuery userInfoQuery = UserInfoQueryConverter.userInfoQuery(request);
        UserInfoDto userInfoDto = userInfoService.getUserInfo(userInfoQuery);
        return UserInfoResponseConverter.getUserInfoResponse(userInfoDto);
    }
}

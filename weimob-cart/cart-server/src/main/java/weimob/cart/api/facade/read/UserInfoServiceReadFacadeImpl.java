package weimob.cart.api.facade.read;

import cart.response.Response;
import com.alibaba.dubbo.config.annotation.Service;
import com.sun.org.apache.bcel.internal.classfile.Code;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import weimob.cart.api.facade.UserInfoServiceReadFacade;
import weimob.cart.api.request.UserInfoRequest;
import weimob.cart.api.response.UserInfo;
import weimob.cart.server.domain.dto.UserInfoDto;
import weimob.cart.server.manager.UserManager;
import weimob.cart.server.query.UserInfoQuery;

/**
 * @Author: 老张
 * @Date: 2020/3/28
 */
@Service
public class UserInfoServiceReadFacadeImpl implements UserInfoServiceReadFacade {
    @Autowired
    private UserManager userManager;
    @Override
    public Response<UserInfo> getUserInfo(UserInfoRequest request) {
        Response<UserInfo> userInfoResponse=new Response<>();
        UserInfo userInfo = new UserInfo();
        UserInfoQuery userInfoQuery = new UserInfoQuery();
        userInfoQuery.setPassword(request.getPassword());
        userInfoQuery.setPhoneNumber(request.getPhoneNumber());
        UserInfoDto userInfoDto = userManager.getUserInfo(userInfoQuery);
        if(userInfoDto==null){
            userInfoResponse.setError(HttpStatus.NOT_FOUND.value()+"","密码或者电话号错误!");
            return userInfoResponse;
        }
        BeanUtils.copyProperties(userInfoDto,userInfo);
        userInfoResponse.setResult(userInfo);
        return userInfoResponse;
    }
}

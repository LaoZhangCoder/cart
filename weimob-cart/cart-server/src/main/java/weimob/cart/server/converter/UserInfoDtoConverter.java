package weimob.cart.server.converter;

import org.springframework.beans.BeanUtils;
import weimob.cart.server.domain.dto.UserInfoDto;
import weimob.cart.server.domain.model.UserInfoDo;

/**
 * @Author: 老张
 * @Date: 2020/4/11
 */
public class UserInfoDtoConverter {

    public static UserInfoDto userInfoDto(UserInfoDo userInfoDo) {
        UserInfoDto userInfoDto = new UserInfoDto();
        BeanUtils.copyProperties(userInfoDo, userInfoDto);
        return userInfoDto;

    }
}

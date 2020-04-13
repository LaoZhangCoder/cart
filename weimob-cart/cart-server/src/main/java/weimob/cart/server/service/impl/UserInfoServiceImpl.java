package weimob.cart.server.service.impl;

import cart.exception.ServiceException;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weimob.cart.server.converter.UserInfoDtoConverter;
import weimob.cart.server.dao.UserInfoDao;
import weimob.cart.server.domain.dto.UserInfoDto;
import weimob.cart.server.domain.model.UserInfoDo;
import weimob.cart.server.query.UserInfoQuery;
import weimob.cart.server.service.UserInfoService;

import java.util.HashMap;

/**
 * @Author: 老张
 * @Date: 2020/3/28
 */
@Service
@Slf4j
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoDao userInfoDao;

    @Override
    public UserInfoDto getUserInfo(UserInfoQuery request) {
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("phoneNumber", request.getPhoneNumber());
        map.put("password", request.getPassword());
        UserInfoDo userInfoDo = this.userInfoDao.findByUniqueIndex(map);
        if (userInfoDo == null) {
            log.warn("不存在该用户!",request.toString());
            throw new ServiceException("用户名或者密码错误!");
        }
        UserInfoDto userInfoDto = UserInfoDtoConverter.userInfoDto(userInfoDo);
        return userInfoDto;
    }
}

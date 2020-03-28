package web.controller;

import cart.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.manager.UserManager;
import web.query.UserLoginQuery;
import web.response.UserInfoVo;
/**
 * @Author: 老张
 * @Date: 2020/3/28
 */
@RestController
@RequestMapping(value = "/api/user/")
@Slf4j
public class UserController {
    @Autowired
    private UserManager userManager;

    @PostMapping(value = "login")
    public Response<UserInfoVo> checkLogin(@RequestBody UserLoginQuery request) {
        Response<UserInfoVo> userInfo = userManager.getUserInfo(request);
        return userInfo;
    }
}

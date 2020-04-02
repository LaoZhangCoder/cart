package web.controller;

import cart.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.manager.UserManager;
import web.query.UserLoginQuery;
import web.response.UserInfoVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
    public Response<UserInfoVo> checkLogin(@RequestBody UserLoginQuery userLoginQuery, HttpServletRequest request, HttpServletResponse response) {
        Response<UserInfoVo> userInfo = null;
        try {
            userInfo = userManager.getUserInfo(userLoginQuery,request,response);
        } catch (IOException e) {
            userInfo.setError("Json转换数据出错");
            e.printStackTrace();
        }
        return userInfo;
    }
}

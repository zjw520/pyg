package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Provinces;
import com.pinyougou.pojo.User;
import com.pinyougou.service.SmsService;
import com.pinyougou.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.method.P;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Reference(timeout = 10000)
    private UserService userService;

    @Reference(timeout = 10000)
    private SmsService smsService;
    @Reference(timeout = 10000)
    private HttpServletRequest request;


    @PostMapping("/save")
    private boolean save(@RequestBody User user, String smsCode) {
        try {
            boolean ok = userService.checkSmsCode(user.getPhone(), smsCode);
            if (ok) {
                userService.save(user);
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @GetMapping("/sendCode")
    private boolean sendCode(String phone) {
        try {
            if (StringUtils.isNoneBlank(phone)) {
                userService.sendCode(phone);
                return true;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @GetMapping("/showName")
    public Map<String, String> showName() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String, String> data = new HashMap<>();
        data.put("loginName", name);
        return data;
    }

    @GetMapping("/findUser")
    public User findUser(HttpServletRequest request){
        try {
            String username = request.getRemoteUser();
            User user = userService.findUser(username);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/updateUser")
    public boolean updateUser(@RequestBody User user){
        try {
            userService.updateUser(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @GetMapping("/sendCodee")
    private boolean sendCodee(String phone) {
        try {
            if (StringUtils.isNoneBlank(phone)) {
                //发送验证码
                userService.sendCodee(phone);
                return true;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    //提交用户
    @PostMapping("/tianjia")
    public boolean tianjia(HttpServletRequest request, @RequestBody Map<String,String> map) {
        try {
            String username = request.getRemoteUser();
            String password = map.get("password");
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            return userService.tianjia(user);
        } catch (Exception ex) {
            return false;
        }
    }
    //绑定手机
    @PostMapping("/updphone")
    public boolean updphone(HttpServletRequest request, @RequestBody Map<String,String> map){
        try{
            String username = request.getRemoteUser();
            String phone = map.get("phone");
            User user = new User();
            user.setUsername(username);
            user.setPhone(phone);
            return userService.updphone(user);
        }catch (Exception ex){
            return false;
        }
    }
    //页面显示电话号码
    @GetMapping("/showbiao")
    public User showtable(HttpServletRequest request){
        try {
            String username = request.getRemoteUser();
            return userService.showtable(username);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

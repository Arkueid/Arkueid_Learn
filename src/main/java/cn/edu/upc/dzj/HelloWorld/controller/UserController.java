package cn.edu.upc.dzj.HelloWorld.controller;


import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson2.JSONObject;

import cn.edu.upc.dzj.HelloWorld.DAO.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

@Controller
@RequestMapping("/users")
public class UserController {

    //设置个人资料
    @RequestMapping(value = "updateinfo", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject updateInfo(
        @RequestParam("username") String username,
        @RequestParam("gender") String gender,
        @RequestParam("email") String email,
        @RequestParam("birthday") String birthday,
        @RequestParam("selfintro") String selfintro
    ) throws SQLException {
        JSONObject res = new JSONObject();
        User user = new User(email);
        user.setUsername(username);
        user.setGender(gender.equals("male")? "0" : "1");
        user.setBirthday(birthday);
        user.setSelfintro(selfintro);
        res.put("code", 0);
        res.put("message", "success");
        user.close();
        return res;
    }

    //上传头像
    @RequestMapping(value = "uploadavatar", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject uploadAvatar(
            HttpServletRequest request,
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileType") String fileType,
            @RequestParam("id") String id
    ) throws Exception {
        String url = "images/default/" + id + ".avatar";
        ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
        if (classLoader==null) {
            JSONObject res = new JSONObject();
            res.put("code", 1);
            res.put("message", "error from server");
            return res;
        }
        String path = classLoader.getResource("static").getPath() + "/" + url;
        file.transferTo(new File(path));
        User user = new User(id);
        user.setAvatar("../" + url);
        user.close();
        JSONObject res = new JSONObject();
        res.put("code", 0);
        res.put("message", "success");
        res.put("url", "../" + url);
        return res;
    }

    //注销登录
    @RequestMapping("logout")
    public void logOut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie: cookies)
        {
            cookie.setMaxAge(0);
            cookie.setValue("");
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        response.sendRedirect("/");
    }
    //查询个人资料
    @RequestMapping("userinfo")
    @ResponseBody
    public JSONObject getUserInfo(
        @RequestParam("email") String email
    ) throws Exception{
        JSONObject res = new JSONObject();
        User user = new User(email);
        res.put("username", user.getUsername());
        res.put("email", user.getEmail());
        res.put("birthday", user.getBirthday());
        res.put("gender", user.getGender());
        res.put("selfintro", user.getSelfintro());
        res.put("avatar", user.getAvatar());
        user.close();
        return res;
    }
}

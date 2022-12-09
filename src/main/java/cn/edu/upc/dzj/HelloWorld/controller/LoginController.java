package cn.edu.upc.dzj.HelloWorld.controller;


import cn.edu.upc.dzj.HelloWorld.Initializer;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.Date;

@Controller
@RequestMapping("/login")
public class LoginController {

    @RequestMapping(value = "submit", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject login(HttpServletRequest request, HttpServletResponse response, @RequestParam("mail") String mail, @RequestParam("password") String password) throws SQLException {
        System.out.println(mail + '\n' + password);
        JSONObject res = new JSONObject();
        if (mail.isEmpty() && password.isEmpty()) return null;
        Statement stmt = null;
        try (Connection conn = DriverManager.getConnection(Initializer.DB_USERS_URL)) {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select password from userinfo where email='" + mail + "'");
            if (rs.next()) {
                if (password.equals(rs.getString("password"))) {
                    Cookie[] cookies = request.getCookies();
                    if (cookies != null) {
                        for (Cookie cookie : cookies) {
                            cookie.setMaxAge(0);
                            response.addCookie(cookie);
                        }
                    }
                    Cookie cookie = new Cookie("id", mail);
                    cookie.setMaxAge(30 * 24 * 60 * 60);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    res.put("code", 0);
                    res.put("type", "login");
                    res.put("message", "success");
                    res.put("redirect", "/dictionary");
                    res.put("ct", new java.util.Date());
                } else {
                    res.put("code", 1);
                    res.put("type", "login");
                    res.put("message", "wrong password");
                    res.put("ct", new java.util.Date());
                }
            }else {
                res.put("code", 0);
                res.put("type", "login");
                res.put("message", "no such user");
                res.put("ct", new java.util.Date());
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(0);
            res.put("code", 1);
            res.put("type", "login");
            res.put("message", "unknown error");
            res.put("ct", new Date());
            System.out.println("unknown error");
        }
        return res;
    }

}

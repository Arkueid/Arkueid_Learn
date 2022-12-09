package cn.edu.upc.dzj.HelloWorld.interceptor;

import cn.edu.upc.dzj.HelloWorld.Initializer;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


@Component
//未登录的时候的拦截器
public class NonLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            response.sendRedirect("/");
            return false;
        }
        else {
            Connection connection = DriverManager.getConnection(Initializer.DB_USERS_URL);
            Statement statement = connection.createStatement();
            for (Cookie cookie : cookies) {
                ResultSet rs = statement.executeQuery("select count(1) from userinfo where email='" + cookie.getValue() + "'");
                rs.next();
                if (cookie.getName() != null && !"".equals(cookie.getName()) && cookie.getName().equals("id") && rs.getInt("count(1)") > 0) {
                    statement.close();
                    connection.close();
                    return true;
                }
            }
            response.sendRedirect("/");
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {

    }
}

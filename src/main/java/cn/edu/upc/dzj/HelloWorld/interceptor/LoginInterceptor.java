package cn.edu.upc.dzj.HelloWorld.interceptor;

import cn.edu.upc.dzj.HelloWorld.Initializer;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override//登录拦截
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {//没有cookie标记不允许访问
            return true;
        }
        else {
            Connection connection = DriverManager.getConnection(Initializer.DB_USERS_URL);
            Statement statement = connection.createStatement();
            for (Cookie cookie : cookies) {
                //cookie存在字段id且id的值在用户数据表中存在则不允许访问，其他情况允许访问
                ResultSet rs = statement.executeQuery("select count(1) from userinfo where email='" + cookie.getValue() + "'");
                rs.next();
                // System.out.println(rs.getInt("id"));
                if (cookie.getName() != null && cookie.getName().equals("id") && rs.getInt("count(1)") > 0 ) {
                    statement.close();
                    connection.close();
                    response.sendRedirect("/dictionary");
                    return false;
                }
            }
            return true;
        }
    }
}

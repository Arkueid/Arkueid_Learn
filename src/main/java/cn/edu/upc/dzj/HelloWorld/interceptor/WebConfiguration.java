package cn.edu.upc.dzj.HelloWorld.interceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
            //未登录不能访问词典页面
            registry.addInterceptor(new NonLoginInterceptor()).addPathPatterns("/dictionary");//没有服务器给予cookie的情况下，阻止/dictionary下所有路径的访问
            //登录后不能访问登录注册页面
            registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/");  //带有服务器给予的cookie的情况下，阻止访问登录注册页面（重定向至/dictionary）
    }
}
package cn.edu.upc.dzj.HelloWorld;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/")
public class Service {
    //首页
    @RequestMapping("/")
    public String register(){
        return "web/index.html";
    }


}


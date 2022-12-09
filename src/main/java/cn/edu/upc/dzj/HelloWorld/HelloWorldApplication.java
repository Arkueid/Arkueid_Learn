package cn.edu.upc.dzj.HelloWorld;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.util.unit.DataSize;


@SpringBootApplication
public class HelloWorldApplication {
	//程序入口
	public static void main(String[] args) throws Exception {
		Initializer.checkDB();
		SpringApplication.run(HelloWorldApplication.class, args);
	}

	@Bean //设置一次请求传输文件的大小限制
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		//最单次最大文件大小
		factory.setMaxFileSize(DataSize.parse("200MB")); //KB,MB
		///请求大小
		factory.setMaxRequestSize(DataSize.parse("1000MB"));
		return factory.createMultipartConfig();
	}
}

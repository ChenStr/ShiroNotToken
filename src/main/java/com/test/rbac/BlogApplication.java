package com.test.rbac;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication
//@MapperScan(basePackages = {"com.test.blog.*.dao"})
public class BlogApplication extends SpringBootServletInitializer {

    public static void main(String[] args) throws Exception {
//        PasswordUtils passwordUtils = new PasswordUtils();
//        passwordUtils.test();
        SpringApplication.run(BlogApplication.class, args);
    }

//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        return builder.sources(BlogApplication.class);
//    }
}

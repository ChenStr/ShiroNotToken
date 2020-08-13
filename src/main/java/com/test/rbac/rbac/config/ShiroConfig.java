package com.test.rbac.rbac.config;


import com.test.rbac.shiro.filter.ShiroFilter;
import com.test.rbac.shiro.realm.AuthRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;


@Configuration
public class ShiroConfig {

    /**
     * 创建Shiro启动必要的工厂类，并将其设置到Soring boot容器里供Spring boot使用
     * 使用@Qualifier可以拿到Springboot容器里的元素，Spring boot容器里的元素默认是用方法进行命名
     * @param defaultWebSecurityManager
     * @return
     */

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("defaultWebSecurityManager") DefaultWebSecurityManager defaultWebSecurityManager){
        //创建Shiro启用必要的工厂文件
        ShiroFilterFactoryBean factory = new ShiroFilterFactoryBean();
        //设置自己的Shiro过滤器
        Map<String, Filter> filter = new HashMap<>();
        filter.put("authc",new ShiroFilter());
        factory.setFilters(filter);
        //将自己定义的安全管理器设置到工厂类里
        factory.setSecurityManager(defaultWebSecurityManager);
        //认证与授权的逻辑
        Map<String,String> map = new HashMap<>();
        //访问/main接口时必须进行登陆
        map.put("/basic/main","authc");
        //访问/manage接口时必须要有manage权限才可以
        map.put("/basic/UserController","perms[UserController]");
        //访问/administrator接口时必须要有administrator角色才能访问
        map.put("/basic/test","roles[demo2]");
        //往工厂类里添加你设置的认证与授权规则
        factory.setFilterChainDefinitionMap(map);
        //设置登陆页面
        factory.setLoginUrl("/basic/login");
        //设置未授权页面
        factory.setUnauthorizedUrl("/basic/notAuth");
        //将创建的安全管理器返回
        return factory;
    }

    /**
     * 创建Shiro的安全管理器并将其丢到Spring boot容器里供Spring boot使用
     * 使用@Qualifier可以拿到Spring boot容器里的元素，Spring boot容器里的元素默认是用方法进行命名
     * @param authRealm
     * @return
     */
    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(@Qualifier("authRealm") AuthRealm authRealm){
        //创建Shiro的安全管理器
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        //将自己定义的模块设置到安全管理器里
        manager.setRealm(authRealm);
        //将创建的安全管理器返回
        return manager;
    }

    /**
     * 创建容器将他丢到Spring boot的IOC容器里，这样Spring boot才能使用他
     * @return
     */
    @Bean
    public AuthRealm authRealm(){
        return new AuthRealm();
    }


    /**
     * thymeleaf整合Shiro的类
     */
//    @Bean
//    public ShiroDialect shiroDialect(){
//        return new ShiroDialect();
//    }

}

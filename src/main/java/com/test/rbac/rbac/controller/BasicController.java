package com.test.rbac.rbac.controller;

import com.test.rbac.common.dto.CommonReturn;
import com.test.rbac.common.other.ErrorCode;
import com.test.rbac.rbac.dto.UserDTO;
import com.test.rbac.rbac.entity.UserEntity;
import com.test.rbac.rbac.service.TokenService;
import com.test.rbac.tools.password.PasswordUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.Account;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RequestMapping("/basic")
@RestController
public class BasicController{

    @Autowired
    //引入Spring data 的Redis操作组件，配置已经自动配置好了，直接注入即可
    private RedisTemplate redisTemplate;

    @Autowired
    TokenService tokenService;

    @Autowired
    HttpServletResponse response;

    @GetMapping("/main")
    public ErrorCode main(UserDTO userDTO){
        ErrorCode code = ErrorCode.ParameterError;
        code.setData(userDTO);
        return code;
    }
    @PostMapping("/UserController")
    public ErrorCode manage(@RequestBody UserDTO userDTO){
        ErrorCode code = ErrorCode.ParameterError;
        code.setData(userDTO);
        return code;
    }
    @PutMapping("/test")
    public ErrorCode admin(@RequestBody UserDTO userDTO){
        ErrorCode code = ErrorCode.ParameterError;
        code.setData(userDTO);
        return code;
    }

    @GetMapping("/login")
    public CommonReturn Login(){
        CommonReturn result = new CommonReturn();
        result.setAll(11111,null,"这是一个登录页面!");
        return result;
    }

    @GetMapping("/notAuth")
    public CommonReturn notAuth(){
        CommonReturn result = new CommonReturn();
        result.setAll(22222,null,"你没有这个授权啊，憨批！");
        return result;
    }

    @PostMapping("/postlogin")
    public Object Postlogin(@RequestBody UserDTO userDTO){
        //使用Subject来存放用户信息
        Subject subject = SecurityUtils.getSubject();
        //使用Shiro的UsernamePasswordToken来保存用户传入的信息
        UsernamePasswordToken token = new UsernamePasswordToken(userDTO.getUsername(),userDTO.getPassword());
        //将验证的操作交给Shiro去完成，我们负责捕获异常即可
        try{
            subject.login(token);
            //得到用户信息
            UserEntity userEntity = (UserEntity) subject.getPrincipal();
            //登录完成后需要在数据库里修改或者更新token必要的话将token存入redis里
            Map<String,Object> tokenmap = tokenService.createToken(userEntity.getId());
            response.setHeader("token", String.valueOf(tokenmap.get("token")));
            //将用户的token存入redis里
            redisTemplate.opsForValue().set(userEntity.getId()+"token",tokenmap.get("token"),72000, TimeUnit.SECONDS);
            //获取session
//            subject.getSession();
            //往session里面传入值
//            subject.getSession().setAttribute("user",userEntity);
            return subject.getSession();
//            return ErrorCode.Success;
        }catch (Exception e){
            e.printStackTrace();
        }
        //用户名不存在
        return ErrorCode.ParameterError;
    }
    @GetMapping("/test")
    public Object test(String token){
        return tokenService.see(token);
    }

    @GetMapping("/logout")
    public ErrorCode logout(){
        //使用Subject来存放用户信息
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return ErrorCode.Success;
    }
}

package com.test.rbac.shiro.filter;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.test.rbac.common.dto.CommonReturn;
import com.test.rbac.rbac.entity.TokenEntity;
import com.test.rbac.rbac.entity.UserEntity;
import com.test.rbac.rbac.service.MenuService;
import com.test.rbac.rbac.service.RoleService;
import com.test.rbac.rbac.service.TokenService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class ShiroFilter extends FormAuthenticationFilter {

    @Autowired
    TokenService tokenService;

    /**
     * 是否允许访问，只要请求头里有token就允许访问
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        //获取请求头里的token
        String token = getRequestToken((HttpServletRequest) request);
        //获取用户的访问地址
        String login = ((HttpServletRequest) request).getServletPath();

        //判断请求头里的token是否为空
        if (StringUtils.isBlank(token)){
            return false;
        }

        return super.isAccessAllowed(request, response, mappedValue);
    }

    /**
     * 拒绝访问方法
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        return super.onAccessDenied(request, response);
    }

    public static String getRequestToken(HttpServletRequest request){
        //默认从请求头中获得token
        String token = request.getHeader("token");
        //如果header中不存在token，则从参数中获取token
        if(StringUtils.isBlank(token)){
            token = request.getParameter("token");
        }
        return token;
    }
}

package com.test.rbac.shiro.realm;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.test.rbac.rbac.dto.MenuDTO;
import com.test.rbac.rbac.dto.RoleDTO;
import com.test.rbac.rbac.dto.TokenDTO;
import com.test.rbac.rbac.dto.UserDetailed;
import com.test.rbac.rbac.entity.*;
import com.test.rbac.rbac.service.*;
import com.test.rbac.shiro.filter.ShiroFilter;
import com.test.rbac.tools.password.PasswordUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.hibernate.validator.internal.util.privilegedactions.GetMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.management.relation.Role;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AuthRealm extends AuthorizingRealm {

    @Autowired
    UserService userService;

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    RoleService roleService;

    @Autowired
    RoleMenuService roleMenuService;

    @Autowired
    MenuService menuService;

    @Autowired
    HttpServletRequest request;

    @Autowired
    TokenService tokenService;


    /**
     * 授权逻辑(登陆后的判断权限)
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //从SecurityUtils工具类中拿取到Subject
        Subject subject = SecurityUtils.getSubject();

        //得到用户信息
        UserEntity userEntity = (UserEntity) subject.getPrincipal();

//        //通过用户信息去查找角色
//        if (userEntity==null) {
//            return null;
//        }
//        Set<String> roles = new HashSet<>();
//        Set<String> menus = new HashSet<>();
//        //判断用户是不是超级管理员
//        if(userEntity.getSuperAdmin()==1){
//            //查找所有角色与权限
//            QueryWrapper<RoleEntity> roleEntityQueryWrapper = new QueryWrapper<>();
//            QueryWrapper<MenuEntity> menuEntityQueryWrapper = new QueryWrapper<>();
//            List<RoleDTO> listroles = roleService.select(roleEntityQueryWrapper);
//            List<MenuDTO> listmenus = menuService.select(menuEntityQueryWrapper);
//            roles = listroles.stream().map(item-> item.getName()).collect(Collectors.toSet());
//            menus = listmenus.stream().map(item-> item.getUrl()).collect(Collectors.toSet());
//        }else{
//            //存放用户所有角色信息
//            Set<RoleDTO> setroles = new HashSet<>();
//            Set<MenuDTO> setmenus = new HashSet<>();
//            //查找用户角色信息
//            QueryWrapper<UserRoleEntity> userRoleEntityQueryWrapper = new QueryWrapper<>();
//            List<UserRoleEntity> userRoles = userRoleService.list(userRoleEntityQueryWrapper.eq("user_id",userEntity.getId()));
//            //拿出userRole的role_id属性
//            List<Long> ids = userRoles.stream().map(item -> item.getRoleId()).collect(Collectors.toList());
//            if (ids.size()<=0){
//                roles = null;
//            }else{
//                List<RoleDTO> listroles = roleService.selectByIds(ids);
//                //去掉重复的角色
//                setroles = new HashSet(listroles);
//                roles = setroles.stream().map(item->item.getName()).collect(Collectors.toSet());
//            }
//            //通过用户角色信息来获取用户权限
//            List<RoleMenuEntity> roleMenuEntities = new ArrayList<>();
//            //假如角色为空
//            if (setroles!=null){
//                //获取用户角色中角色所有的权限id
//                ids = setroles.stream().map(item -> item.getId()).collect(Collectors.toList());
//                QueryWrapper<RoleMenuEntity> roleMenuEntityQueryWrapper = new QueryWrapper<>();
//                List<RoleMenuEntity> roleMenus = new ArrayList<>();
//                roleMenus = roleMenuService.list(roleMenuEntityQueryWrapper.in("role_id",ids));
//                roleMenuEntities.addAll(roleMenus);
//            }
//            //else里的代码可以存放用户直接与权限关联的逻辑
//
//            if (roleMenuEntities==null || roleMenuEntities.size() <= 0){
//                menus = null;
//            }else{
//                ids = roleMenuEntities.stream().map(item -> item.getMenuId()).collect(Collectors.toList());
//                List<MenuDTO> listmenus = menuService.selectByIds(ids);
//                setmenus = new HashSet(listmenus);
//                menus = setmenus.stream().map(item->item.getUrl()).collect(Collectors.toSet());
//            }
//        }

        Set<String> roles = new HashSet<>();
        Set<String> menus = new HashSet<>();
        String token = ShiroFilter.getRequestToken(request);
        UserDetailed user = (UserDetailed) tokenService.see(token).getData();
        //判断token是否有效
        if (user==null){
            return null;
        }
        //判断token的所有者是否跟数据库里的一致
        if (user.getUserDTO().getId()!=userEntity.getId()){
            return null;
        }
        //将用户的角色与权限拿出来
        roles = user.getRoleDTOS().stream().map(item->item.getName()).collect(Collectors.toSet());
        menus = user.getMenuDTOS().stream().map(item->item.getUrl()).collect(Collectors.toSet());


        //从Subject类中拿取用户信息
        SimpleAuthorizationInfo account = new SimpleAuthorizationInfo();

        //设置角色
        account.addRoles(roles);

        //设置权限
        account.addStringPermissions(menus);

        return account;
    }

    /**
     * 认证逻辑(登陆)
     * @param authenticationToken  （记录了用户的用户名与密码）
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String accessToken = (String) authenticationToken.getPrincipal();
        //将用户信息保存在Shiro容器里
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        //判断密码或用户名是否为空
        if (token.getPassword()==null || token.getUsername()==null){
            return null;
        }
        QueryWrapper<UserEntity> userQueryWrapper = new QueryWrapper();
        UserEntity userEntity = userService.getOne(userQueryWrapper.eq("username",token.getUsername()));

        //判断用户是否存在
        if (userEntity!=null){
            //判断密码是否正确的方法交给自己来写if里面就是
            if (PasswordUtils.matches(String.valueOf(token.getPassword()),userEntity.getPassword())){
                return new SimpleAuthenticationInfo(userEntity,token.getPassword(),getName());
            }else{
                return null;
            }
        }
        return null;
    }

}

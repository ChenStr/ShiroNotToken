package com.test.rbac.rbac.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.test.rbac.common.dto.CommonReturn;
import com.test.rbac.common.service.impl.BaseServiceImpl;
import com.test.rbac.rbac.dao.UserDao;
import com.test.rbac.rbac.dto.UserDTO;
import com.test.rbac.rbac.entity.RoleMenuEntity;
import com.test.rbac.rbac.entity.TokenEntity;
import com.test.rbac.rbac.entity.UserEntity;
import com.test.rbac.rbac.entity.UserRoleEntity;
import com.test.rbac.rbac.service.*;
import com.test.rbac.shiro.filter.ShiroFilter;
import com.test.rbac.tools.password.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author DNYY
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<UserDao, UserEntity, UserDTO> implements UserService {

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    TokenService tokenService;

    @Autowired
    RoleService roleService;

    @Autowired
    RoleMenuService roleMenuService;

    @Autowired
    MenuService menuService;

    @Autowired
    HttpServletRequest request;

    /**
     * 在添加信息前加密密码
     * @param dto
     */
    @Override
    public void beforeInsert(UserDTO dto) {
        //添加创建时间与更新时间
        Date date = new Date();
        dto.setCreateDate(date);
        dto.setUpdateDate(date);
        //添加修改人与创建人

        //获取用户token
        String token = ShiroFilter.getRequestToken(request);
        //通过用户的token来查找用户的id
        QueryWrapper<TokenEntity> tokenQueryWrapper = new QueryWrapper<>();
        TokenEntity tokenEntity = tokenService.getOne(tokenQueryWrapper.eq("token",token));
        //如果查找的到的话
        if(tokenEntity!=null){
            //添加修改人与创建人
            dto.setCreator(tokenEntity.getUserId());
            dto.setUpdater(tokenEntity.getUserId());
        }
    }

    @Override
    public void beforEedit(UserDTO dto) {
        //添加更新时间
        Date date = new Date();
        dto.setUpdateDate(date);
        //添加修改人

        //获取用户token
        String token = ShiroFilter.getRequestToken(request);
        //通过用户的token来查找用户的id
        QueryWrapper<TokenEntity> tokenQueryWrapper = new QueryWrapper<>();
        TokenEntity tokenEntity = tokenService.getOne(tokenQueryWrapper.eq("token",token));
        //如果查找的到的话
        if(tokenEntity!=null){
            //添加修改人
            dto.setUpdater(tokenEntity.getUserId());
        }
    }

    /**
     * 登陆方法
     * @param userDTO
     * @return CommonReturn
     */
    @Override
    public CommonReturn login(UserDTO userDTO) {
        CommonReturn result = new CommonReturn();
        //判断密码或用户名是否为空
        if (StringUtils.isEmpty(userDTO.getPassword()) || StringUtils.isEmpty(userDTO.getUsername())){
            result.setAll(10001,null,"参数错误,请仔细检查您输入的参数!");
            return result;
        }
        QueryWrapper<UserEntity> userQueryWrapper = new QueryWrapper<>();
        UserEntity userEntity = this.getOne(userQueryWrapper.eq("username",userDTO.getUsername()));
        //判断用户是否存在
        if (userEntity==null){
            result.setAll(30001,null,"用户名或者密码错误请仔细检查");
            return result;
        }

        //判断密码是否正确
        if(!PasswordUtils.matches(userDTO.getPassword(),userEntity.getPassword())){
            result.setAll(30001, PasswordUtils.encode(userDTO.getPassword()),"用户名或者密码错误请仔细检查");
            return result;
        }
        //判断账号是否停用了
        if (userEntity.getStatus() == 0){
            result.setAll(30002,null,"账号已被停用!");
            return result;
        }

        //登陆成功创建用户Token
        Map<String,Object> token = new HashMap<>();
        token = tokenService.createToken(userEntity.getId());
        result.setAll(20000,token,"登陆成功");
        return result;
    }

    @Override
    public CommonReturn editUser(UserDTO userDTO) {
        CommonReturn result = new CommonReturn();
        //用户设置不了
        userDTO.setUpdater(null);
        userDTO.setUpdateDate(null);
        userDTO.setStatus(null);
        userDTO.setSuperAdmin(null);
        userDTO.setUsername(null);
        if (userDTO.getPassword()!=null){
            //将密码加密
            String password = PasswordUtils.encode(userDTO.getPassword());
            userDTO.setPassword(password);
        }
        try{
            edit(userDTO);
            //拿到用户修改后的数据
            UserDTO user = selectById(userDTO.getId());
            //考虑是否要显示全部内容给用户看

            result.setAll(20000,user,"编辑成功");
        }catch (Exception e){
            result.setAll(10001,null,"参数错误");
        }
        return result;
    }

    /**
     * 注册方法
     * @param userDTO
     * @return CommonReturn
     */
    @Override
    public CommonReturn register(UserDTO userDTO) {
        CommonReturn result = new CommonReturn();
        //状态、与修改创建相关信息不能交由用户操作 加密用户输入的密码
        userDTO.setStatus(null);
        userDTO.setCreator(null);
        userDTO.setCreateDate(null);
        userDTO.setUpdater(null);
        userDTO.setUpdateDate(null);
        userDTO.setSuperAdmin(0);
        String password = PasswordUtils.encode(userDTO.getPassword());
        userDTO.setPassword(password);
        try{
            this.insert(userDTO);
            result.setAll(20000,userDTO,"操作成功");
        }catch (Exception e){
            result.setAll(10001,null,"参数错误");
        }
        return result;
    }
}

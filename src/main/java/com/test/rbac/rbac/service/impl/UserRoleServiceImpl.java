package com.test.rbac.rbac.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.test.rbac.common.dto.CommonReturn;
import com.test.rbac.common.service.impl.BaseServiceImpl;
import com.test.rbac.rbac.dao.UserRoleDao;
import com.test.rbac.rbac.dto.RoleDTO;
import com.test.rbac.rbac.dto.ToListDTO;
import com.test.rbac.rbac.dto.UserDTO;
import com.test.rbac.rbac.dto.UserRoleDTO;
import com.test.rbac.rbac.entity.RoleEntity;
import com.test.rbac.rbac.entity.TokenEntity;
import com.test.rbac.rbac.entity.UserEntity;
import com.test.rbac.rbac.entity.UserRoleEntity;
import com.test.rbac.rbac.service.RoleService;
import com.test.rbac.rbac.service.TokenService;
import com.test.rbac.rbac.service.UserRoleService;
import com.test.rbac.rbac.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author DNYY
 */
@Service
public class UserRoleServiceImpl extends BaseServiceImpl<UserRoleDao, UserRoleEntity, UserRoleDTO> implements UserRoleService {

    @Autowired
    TokenService tokenService;

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Override
    public void beforeInsert(UserRoleDTO dto) {
        //添加创建时间
        Date date = new Date();
        dto.setCreateDate(date);
        //添加创建人
        String token = dto.getToken();
        //通过用户的token来查找用户的id
        QueryWrapper<TokenEntity> tokenQueryWrapper = new QueryWrapper<>();
        TokenEntity tokenEntity = tokenService.getOne(tokenQueryWrapper.eq("token",token));
        //如果查找的到的话
        if(tokenEntity!=null){
            //添加修改人与创建人
            dto.setCreator(tokenEntity.getUserId());
        }
    }

    @Override
    public void beforEedit(UserRoleDTO dto) {

    }

    @Override
    public CommonReturn getUserRole(UserRoleDTO userRoleDTO) {
        CommonReturn result = new CommonReturn();
        Map<String,Object> data = new HashMap<>();
        //判断userId与roleId是否有值
        if (userRoleDTO.getRoleId()!=null){
            //首先找到userId
            data.put("role_id",userRoleDTO.getRoleId());
            List<UserRoleDTO> users = this.select(data);
            List<Long> userIds = new ArrayList<>();
            users.stream().forEach(i->userIds.add(i.getUserId()));
            QueryWrapper<UserEntity> userQueryWrapper = new QueryWrapper<>();
            List<UserDTO> userDTOS = userService.selectPage(userRoleDTO.getPage(),userRoleDTO.getPageSize(),userQueryWrapper.in("id",userIds));
            if(userDTOS==null){
                result.setAll(10001,null,"参数错误");
            }else{
                result.setAll(20000,userDTOS,"操作成功");
            }
        }else if (userRoleDTO.getUserId()!=null){
            //首先找到roleId
            data.put("user_id",userRoleDTO.getUserId());
            List<UserRoleDTO> roles = this.select(data);
            List<Long> roleIds = new ArrayList<>();
            roles.stream().forEach(i->roleIds.add(i.getRoleId()));
            QueryWrapper<RoleEntity> queryWrapper = new QueryWrapper<>();
            List<RoleDTO> roleDTOS = roleService.selectPage(userRoleDTO.getPage(),userRoleDTO.getPageSize(),queryWrapper.in("id",roleIds));
            if (roleDTOS==null){
                result.setAll(10001,null,"参数错误");
            }else{
                result.setAll(20000,roleDTOS,"操作成功");
            }
        }else{
            result.setAll(10001,null,"参数错误");
        }
        return result;
    }

    @Override
    public CommonReturn addUserRole(ToListDTO<Long, Long> data) {
        CommonReturn result = new CommonReturn();
        //通过token判断用户是否是超级管理员
        QueryWrapper<TokenEntity> tokenQueryWrapper = new QueryWrapper<>();
        TokenEntity tokenEntity = tokenService.getOne(tokenQueryWrapper.eq("token",data.getToken()));
        UserDTO userDTO = userService.selectById(tokenEntity.getUserId());
        //判断是否是超级管理员
        if(userDTO.getSuperAdmin()==1) {
            //判断传过来的值是否为空
            if (data == null) {
                result.setAll(10001, null, "参数错误");
            } else {
                //判断值是否为空
                if (data.getData() == null || data.getData2() == null || data.getData().size() == 0 || data.getData2().size() == 0) {
                    result.setAll(10001, null, "参数错误");
                } else {
                    //判断这些userId与roleId是否都有值没有则不让其添加
                    QueryWrapper<UserEntity> userqueryWrapper = new QueryWrapper<>();
                    QueryWrapper<RoleEntity> rolequeryWrapper = new QueryWrapper<>();
                    List<UserEntity> users = userService.list(userqueryWrapper.in("id",data.getData()));
                    List<RoleEntity> roles = roleService.list(rolequeryWrapper.in("id",data.getData2()));
                    if(users!=null && roles!=null && users.size() > 0 && roles.size() > 0){
                        for (int i = 0; i < data.getData().size(); i++) {
                            for (int j = 0; j < data.getData2().size(); j++) {
                                UserRoleDTO userRoleDTO = new UserRoleDTO();
                                userRoleDTO.setToken(data.getToken());
                                userRoleDTO.setUserId(data.getData().get(i));
                                userRoleDTO.setRoleId(data.getData2().get(j));
                                Map<String,Object> map = new HashMap<>();
                                map.put("user_id",data.getData().get(i));
                                map.put("role_id",data.getData2().get(j));
                                List<UserRoleDTO> da = this.select(map);
                                if (da==null || da.size()<=0){
                                    this.insert(userRoleDTO);
                                }
                                //测试代码
//                            System.out.println(da.size()+"-----"+da==null);
                            }
                        }
                        result.setAll(20000,data,"操作成功");
                    }else{
                        result.setAll(10001, null, "参数错误");
                    }
                }
            }
        }else{
            result.setAll(30004,null,"用户没有权限");
        }
        return result;
    }

    @Override
    public CommonReturn delUserRole(ToListDTO<Long, Long> data) {
        CommonReturn result = new CommonReturn();
        //通过token判断用户是否是超级管理员
        QueryWrapper<TokenEntity> tokenQueryWrapper = new QueryWrapper<>();
        TokenEntity tokenEntity = tokenService.getOne(tokenQueryWrapper.eq("token",data.getToken()));
        UserDTO userDTO = userService.selectById(tokenEntity.getUserId());
        //判断是否是超级管理员
        if(userDTO.getSuperAdmin()==1) {
            //判断传过来的值是否为空
            if (data == null) {
                result.setAll(10001, null, "参数错误");
            } else {
                //判断值是否为空
                if (data.getData() == null || data.getData2() == null || data.getData().size() == 0 || data.getData2().size() == 0) {
                    result.setAll(10001, null, "参数错误");
                } else {
                    //判断这些userId与roleId是否都有值没有则不让其添加
                    QueryWrapper<UserEntity> userqueryWrapper = new QueryWrapper<>();
                    QueryWrapper<RoleEntity> rolequeryWrapper = new QueryWrapper<>();
                    List<UserEntity> users = userService.list(userqueryWrapper.in("id", data.getData()));
                    List<RoleEntity> roles = roleService.list(rolequeryWrapper.in("id", data.getData2()));
                    if (users != null && roles != null && users.size() > 0 && roles.size() > 0) {
                        for (int i = 0; i < data.getData().size(); i++) {
                            for (int j = 0; j < data.getData2().size(); j++) {
                                Map<String, Object> map = new HashMap<>();
                                map.put("user_id", data.getData().get(i));
                                map.put("role_id", data.getData2().get(j));
                                List<UserRoleDTO> da = this.select(map);
                                if (da != null || da.size() > 0) {
                                    List<Long> ids = new ArrayList<>();
                                    da.stream().forEach(id -> ids.add(id.getId()));
                                    this.deleteByIds(ids);
                                }
                            }
                        }
                        result.setAll(20000,null,"操作成功");
                    } else{
                        result.setAll(10001, null, "参数错误");
                    }
                }
            }
        }else{
            result.setAll(30004,null,"用户没有权限");
        }
        return result;
    }


}

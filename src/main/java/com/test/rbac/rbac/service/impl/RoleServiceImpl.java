package com.test.rbac.rbac.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.test.rbac.common.dto.CommonReturn;
import com.test.rbac.common.service.impl.BaseServiceImpl;
import com.test.rbac.rbac.dao.RoleDao;
import com.test.rbac.rbac.dto.*;
import com.test.rbac.rbac.entity.RoleEntity;
import com.test.rbac.rbac.entity.TokenEntity;
import com.test.rbac.rbac.service.*;
import com.test.rbac.shiro.filter.ShiroFilter;
import com.test.rbac.tools.my.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author DNYY
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl<RoleDao, RoleEntity, RoleDTO> implements RoleService {

    QueryWrapper<TokenEntity> tokenQueryWrapper = new QueryWrapper<>();

    QueryWrapper<RoleEntity> roleQueryWrapper = new QueryWrapper<>();

    protected Class<RoleDTO> currentDtoClass(){
        return (Class<RoleDTO>) ReflectionKit.getSuperClassGenericType(getClass(), 2);
    }

    @Autowired
    TokenService tokenService;

    @Autowired
    UserService userService;

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    RoleService roleService;

    @Autowired
    RoleMenuService roleMenuService;

    @Autowired
    HttpServletRequest request;

    /**
     * 在插入后的钩子方法
     * @param dto
     */
    @Override
    public void beforeInsert(RoleDTO dto) {
        //添加创建时间与更新时间
        Date date = new Date();
        dto.setCreateDate(date);
        dto.setUpdateDate(date);
        //添加创建人与更新人
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

    /**
     * 在更新后的钩子方法
     * @param dto
     */
    @Override
    public void beforEedit(RoleDTO dto) {
        //添加更新时间
        Date date = new Date();
        dto.setUpdateDate(date);
        //添加更新人
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
     * 添加角色方法
     * @param roleDTO
     * @return
     */
    @Override
    public CommonReturn saveRole(RoleDTO roleDTO) {
        CommonReturn result = new CommonReturn();
        //通过token判断用户是否是超级管理员
        QueryWrapper<TokenEntity> tokenQueryWrapper = new QueryWrapper<>();
        TokenEntity tokenEntity = tokenService.getOne(tokenQueryWrapper.eq("token",ShiroFilter.getRequestToken(request)));
        UserDTO userDTO = userService.selectById(tokenEntity.getUserId());
        if(userDTO.getSuperAdmin()==1){
            //设置用户不允许修改的属性
            roleDTO.setUpdateDate(null);
            roleDTO.setUpdater(null);
            roleDTO.setCreateDate(null);
            roleDTO.setCreator(null);
            try{
                this.insert(roleDTO);
                result.setAll(20000,roleDTO,"操作成功");
            }catch (Exception e){
                result.setAll(10001,null,"参数错误");
            }
        }else{
            result.setAll(30004,null,"用户没有权限");
        }
        return result;
    }

    /**
     * 编辑角色方法
     * @param roleDTO
     * @return
     */
    @Override
    public CommonReturn editRole(RoleDTO roleDTO) {
        CommonReturn result = new CommonReturn();
        //通过token判断用户是否是超级管理员
        QueryWrapper<TokenEntity> tokenQueryWrapper = new QueryWrapper<>();
        TokenEntity tokenEntity = tokenService.getOne(tokenQueryWrapper.eq("token",ShiroFilter.getRequestToken(request)));
        UserDTO userDTO = userService.selectById(tokenEntity.getUserId());
        if(userDTO.getSuperAdmin()==1){
            //设置用户不允许修改的属性
            roleDTO.setUpdateDate(null);
            roleDTO.setUpdater(null);
            roleDTO.setCreateDate(null);
            roleDTO.setCreator(null);
            roleDTO.setName(null);
            try{
                this.edit(roleDTO);
                result.setAll(20000,roleDTO,"操作成功");
            }catch (Exception e){
                result.setAll(10001,null,"参数错误");
            }
        }else{
            result.setAll(30004,null,"没有权限");
        }
        return result;
    }


    /**
     * 根据给定的条件来查找角色的方法
     * @param roleDTO
     * @return
     */
    @Override
    public CommonReturn getRole(RoleDTO roleDTO) {
        CommonReturn result = new CommonReturn();
        Map<String,Object> data = MyUtils.objectToMap(roleDTO);
        data.remove("token");
        List<RoleDTO> roles = this.select(data);
        if(roles.isEmpty()){
            result.setAll(20000,roles,"没有查找结果，建议仔细核对查找条件");
        }else{
            //删去一些不能被用户看见的字段
            RoleDTO roleT = new RoleDTO();
            roleT.setName("test");roleT.setRemark("asd");roleT.setId(11L);
            roles = (List<RoleDTO>) MyUtils.AllSet(roles,roleT);
            result.setAll(20000,roles,"查找成功");
        }
        return result;
    }

    /**
     * 获取全部的角色 分页
     * @param date
     * @return
     */
    @Override
    public CommonReturn getAllRole(RoleDTO date) {
        CommonReturn result = new CommonReturn();
        List<RoleDTO> roleDTOS = this.selectPage(date.getPage(),date.getPageSize(),null);
        if (roleDTOS==null){
            result.setAll(10001,null,"参数错误");
        }else{
            result.setAll(20000,roleDTOS,"查找成功");
        }
        return result;
    }

    @Override
    public CommonReturn getAllRole() {
        CommonReturn result = new CommonReturn();
        QueryWrapper<RoleEntity> wrapper = new QueryWrapper<>();
        List<RoleDTO> roleDTOS = this.select(wrapper);
        if (roleDTOS==null){
            result.setAll(10001,null,"参数错误");
        }else{
            result.setAll(20000,roleDTOS,"查找成功");
        }
        return result;
    }

    @Override
    public CommonReturn delRole(OneListDTO<Long> ids) {
        CommonReturn result = new CommonReturn();
        //通过token判断用户是否是超级管理员
        QueryWrapper<TokenEntity> tokenQueryWrapper = new QueryWrapper<>();
        TokenEntity tokenEntity = tokenService.getOne(tokenQueryWrapper.eq("token",ShiroFilter.getRequestToken(request)));
        UserDTO userDTO = userService.selectById(tokenEntity.getUserId());
        if(userDTO.getSuperAdmin()==1){
            //确定身份后开始删除角色
            if(ids!=null){
                if(ids.getData()!=null && ids.getData().size() > 0){
                    List<Long> userRoleids = new ArrayList<>();
                    List<Long> roleMenuids = new ArrayList<>();
                    List<Long> roleIds = new ArrayList<>();
                    for (int i = 0; i < ids.getData().size() ; i++){
                        //查找角色对应id是否有跟用户绑定
                        Map<String,Object> map = new HashMap<>();
                        map.put("role_id",ids.getData().get(i));
                        List<UserRoleDTO> userRoles = userRoleService.select(map);
                        List<RoleMenuDTO> roleMenus = roleMenuService.select(map);
                        if(userRoles!=null && userRoles.size()>0){
                            userRoles.stream().forEach(a->userRoleids.add(a.getId()));
                            roleMenus.stream().forEach(a->roleMenuids.add(a.getId()));
                            roleIds.add(ids.getData().get(i));
                        }
                    }
                    if (userRoleids!=null || roleIds!=null || roleMenuids!=null){
                            userRoleService.deleteByIds(userRoleids);
                            roleMenuService.deleteByIds(roleMenuids);
                            this.deleteByIds(roleIds);
                            result.setAll(20000,ids,"操作成功");
                    }else{
                        result.setAll(10001,null,"参数错误");
                    }
                }else{
                    result.setAll(10001,null,"参数错误");
                }
            }else{
                result.setAll(10001,null,"参数错误");
            }
        }else{
            result.setAll(30004,null,"没有权限");
        }
        return result;
    }
}

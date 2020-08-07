package com.test.rbac.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.test.rbac.common.service.BaseService;
import com.test.rbac.tools.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 浮桥的Service层实现类(方法围绕DTO)
 *
 * M 为 DAO 层
 *
 * T 为 Entity
 *
 * D 为 DTO
 *
 * @author DNYY
 */
public abstract class BaseServiceImpl<M extends BaseMapper<T> , T , D> extends DeepServiceImpl<M,T> implements BaseService<T , D> {

    //获得T(Entity)的类名
    protected Class<T> currentEntityClass() {
        return (Class<T>) ReflectionKit.getSuperClassGenericType(getClass(), 2);
    }

    protected Class<D> currentDtoClass(){
        return (Class<D>) ReflectionKit.getSuperClassGenericType(getClass(), 2);
    }

    /**
     * 保存方法
     * @param dto
     */
    @Override
    public void insert(D dto) {
        //保存方法前使用的方法
        beforeInsert(dto);
        T data = ConvertUtils.convert(dto,currentEntityClass());
        this.save(data);
    }

    /**
     * 通过id查找一条数据
     * @param id
     * @return
     */
    @Override
    public D selectById(Long id){
        T data = this.getById(id);
        D d = ConvertUtils.convert(data,currentDtoClass());
        return d;
    }

    /**
     * 根据许多id来查找多条数据
     * @param ids
     * @return
     */
    @Override
    public List<D> selectByIds(List<Long> ids){
        List<T> data = (List<T>) this.listByIds(ids);
        List<D> nmsl = new ArrayList<>();
        for (T entity : data) {
            D d = ConvertUtils.convert(entity,currentDtoClass());
            nmsl.add(d);
        }
        return nmsl;
    }

    /**
     * 修改方法
     * @param dto
     */
    @Override
    public void edit(D dto){
        //编辑方法前的前置方法
        beforEedit(dto);
        T data = ConvertUtils.convert(dto,currentEntityClass());
        this.updateById(data);
    }


    /**
     * 根据id来删除单条数据
     * @param id
     * @return
     */
    @Override
    public Boolean deleteById(Long id){
        Boolean flag = this.removeById(id);
        return flag;
    }

    /**
     * 根据许多id来删除多条数据
     * @param ids
     * @return
     */
    @Override
    public Boolean deleteByIds(List<Long> ids){
        Boolean flag = this.removeByIds(ids);
        return flag;
    }

    /**
     * 自定义查找方法
     * @param data
     * @return
     */
    @Override
    public List<D> select(Map<String,Object> data){
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        for ( String key : data.keySet() ) {
            if (data.get(key)!=null){
                wrapper.eq(key,data.get(key));
            }
        }
        List<T> list = this.list(wrapper);
        List<D> dList = ConvertUtils.convert(list, currentDtoClass());
        return dList;
    }

    /**
     * 自定义查找方法
     * @param wrapper
     * @return
     */
    @Override
    public List<D> select(QueryWrapper<T> wrapper){
        List<T> list = this.list(wrapper);
        List<D> dList = ConvertUtils.convert(list, currentDtoClass());
        return dList;
    }

    /**
     * mybatisPlus分页方法
     * @param page
     * @return
     */
    @Override
    public List<D> selectPage(Object page,Object pageSize, QueryWrapper<T> queryWrapper){
        if (page == null){
            page = 1;
        }
        if (pageSize == null){
            pageSize = 10;
        }
        int Page,PageSize;
        try{
            Page = (int) page;
            PageSize = (int) pageSize;
        }catch (Exception e){
            page = 1;
            pageSize = 10;
        }
        IPage<T> Ipage = new Page<>((int)page, (int)pageSize);
        //获取页数
        baseMapper.selectPage(Ipage,queryWrapper);
        List<T> pages = Ipage.getRecords();
        List<D> date = ConvertUtils.convert(pages,currentDtoClass());
        return date;
    }








    /**
     * 在添加方法之前执行的方法
     * @param dto
     * @return dto
     */
    public abstract void beforeInsert(D dto);

    /**
     * 在编辑之前执行的方法
     * @param dto
     */
    public abstract void beforEedit(D dto);



}

package com.test.rbac.test.entity;


import java.io.Serializable;
import java.util.Date;

public class Man implements Serializable {

    //主键id
    public Long id;
    //人员姓名
    public String name;
    //人员身高
    public Double height;
    //人员生日
    public Date birthday;
}

package com.test.rbac.rbac.dto;

import com.test.rbac.common.dto.BaseDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OneListDTO<D> extends BaseDTO implements Serializable {

    List<D> data;

}

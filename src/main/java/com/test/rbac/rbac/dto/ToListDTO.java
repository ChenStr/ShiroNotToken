package com.test.rbac.rbac.dto;

import com.test.rbac.common.dto.BaseDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ToListDTO<D,D2> extends BaseDTO implements Serializable {

    List<D> data;

    List<D2> data2;
}

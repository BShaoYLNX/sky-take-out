package com.sky.mapper;

import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealMapper {

    List<SetmealVO> pageQuery(String name, Integer categoryId, Integer status);
}

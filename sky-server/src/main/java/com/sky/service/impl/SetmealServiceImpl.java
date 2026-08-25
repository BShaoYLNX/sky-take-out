package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO pageQueryDTO) {
        // 开始分页
        PageHelper.startPage(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());

        // 执行查询
        List<SetmealVO> list = setmealMapper.pageQuery(
                pageQueryDTO.getName(),
                pageQueryDTO.getCategoryId(),
                pageQueryDTO.getStatus()
        );
        // 封装 PageResult
        Page<SetmealVO> page = (Page<SetmealVO>) list;
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 新增套餐
     * @param setmealDTO
     */
    @Override
    public void addSetmeal(SetmealDTO setmealDTO) {

    }
}

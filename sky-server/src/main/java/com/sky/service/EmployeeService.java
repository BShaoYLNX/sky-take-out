package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 员工分页查询
     * @param pageQueryDTO
     * @return
     */
    PageResult pageQuery(EmployeePageQueryDTO pageQueryDTO);

    /**
     * 新增员工
     * @param employeeDTO
     */
    void addEmployee(EmployeeDTO employeeDTO);

    /**
     * 修改员工方法
     * @param employeeDTO
     */
    void updateEmployee(EmployeeDTO employeeDTO);
}

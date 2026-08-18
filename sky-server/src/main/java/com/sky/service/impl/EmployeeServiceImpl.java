package com.sky.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String unpassword = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // TODO 后期需要进行md5加密，然后再进行比对
        //加密密码
        String password = DigestUtils.md5DigestAsHex(unpassword.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 员工分页查询
     * @param pageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(EmployeePageQueryDTO pageQueryDTO) {
        // 分页查询
        // 使用 PageHelper 插件
        PageHelper.startPage(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());
        List<Employee> employeeList = employeeMapper.pageQuery(pageQueryDTO.getName());

        // 封装 PageResult
        PageInfo<Employee> pageInfo = new PageInfo<>(employeeList);
        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 新增员工
     * @param employeeDTO
     */
    @Override
    public void addEmployee(EmployeeDTO employeeDTO) {
        // DTO 转 Entity
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);

        // 补全系统字段
        // 默认密码：MD5加密后的“123456”
        String defaultPassword = DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes());
        employee.setPassword(defaultPassword);
        employee.setStatus(StatusConstant.ENABLE);  // 1 正常
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        employee.setCreateUser(BaseContext.getCurrentId());
        employee.setUpdateUser(BaseContext.getCurrentId());

        //  调用 Mapper 插入
        employeeMapper.addEmployee(employee);
    }

    @Override
    public void updateEmployee(EmployeeDTO employeeDTO) {
        // DTO 转 Entity
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        // 手动设置更新时间和更新人
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(BaseContext.getCurrentId());

        // 调用 Mapper 更新
        employeeMapper.updateEmployee(employee);
    }

}

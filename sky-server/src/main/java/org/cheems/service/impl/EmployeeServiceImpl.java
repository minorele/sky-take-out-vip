package org.cheems.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.cheems.constant.MessageConstant;
import org.cheems.constant.PasswordConstant;
import org.cheems.constant.StatusConstant;
import org.cheems.context.BaseContext;
import org.cheems.dto.EmployeeDTO;
import org.cheems.dto.EmployeeLoginDTO;
import org.cheems.dto.EmployeePageQueryDTO;
import org.cheems.entity.Employee;
import org.cheems.exception.AccountExistedException;
import org.cheems.exception.AccountLockedException;
import org.cheems.exception.AccountNotFoundException;
import org.cheems.exception.PasswordErrorException;
import org.cheems.mapper.EmployeeMapper;
import org.cheems.result.PageResult;
import org.cheems.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = DigestUtils.md5DigestAsHex(employeeLoginDTO.getPassword().getBytes());

        Employee employee = employeeMapper.getByUsername(username);



        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码对比
        if (!password.equals(employee.getPassword())) {
            //密码错误
           throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (Objects.equals(employee.getStatus(), StatusConstant.DISABLE)) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }


        return employee;
    }
    /**
     *新增员工
     * @param employeeDTO 员工信息
     */
    @Override
    public void add(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        String username = employee.getUsername();
        Employee queryEmployee = employeeMapper.getByUsername(username);
        if (queryEmployee != null){
            throw new AccountExistedException(MessageConstant.ACCOUNT_EXISTED);
        }
        //设置员工账号的状态
        employee.setStatus(StatusConstant.ENABLE);
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        employee.setCreateUser(BaseContext.getCurrentId());
        employee.setUpdateUser(BaseContext.getCurrentId());
        employeeMapper.insert(employee);
    }

    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {

        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());

        Page<Employee> employeePage =  employeeMapper.pageQuery(employeePageQueryDTO);
        long total = employeePage.getTotal();
        List<Employee> records = employeePage.getResult();
        return new PageResult(total,records);
    }
}

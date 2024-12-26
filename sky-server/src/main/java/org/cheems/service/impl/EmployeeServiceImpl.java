package org.cheems.service.impl;

import org.cheems.constant.MessageConstant;
import org.cheems.constant.StatusConstant;
import org.cheems.dto.EmployeeLoginDTO;
import org.cheems.entity.Employee;
import org.cheems.exception.AccountLockedException;
import org.cheems.exception.AccountNotFoundException;
import org.cheems.exception.PasswordErrorException;
import org.cheems.mapper.EmployeeMapper;
import org.cheems.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();
        Employee employee = employeeMapper.getByUsername(username);



        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码对比
        // TODO 后期需要进行md5加密，然后再进行比对
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
}

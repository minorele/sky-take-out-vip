package org.cheems.service;

import org.cheems.dto.EmployeeDTO;
import org.cheems.dto.EmployeeLoginDTO;
import org.cheems.dto.EmployeePageQueryDTO;
import org.cheems.entity.Employee;
import org.cheems.result.PageResult;


public interface EmployeeService {

    Employee login(EmployeeLoginDTO employeeLoginDTO);

    void add(EmployeeDTO employeeDTO);
    void update(EmployeeDTO employeeDTO);

    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    void updateStatus(Integer status, Long id);

    Employee employeeQuery(Long id);
}

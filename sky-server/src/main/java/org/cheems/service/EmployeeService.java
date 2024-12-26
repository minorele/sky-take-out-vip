package org.cheems.service;

import org.cheems.dto.EmployeeLoginDTO;
import org.cheems.entity.Employee;
import org.cheems.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


public interface EmployeeService {

    Employee login(EmployeeLoginDTO employeeLoginDTO);
}

package org.cheems.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.cheems.dto.EmployeeLoginDTO;
import org.cheems.entity.Employee;

@Mapper
public interface EmployeeMapper {

    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

}

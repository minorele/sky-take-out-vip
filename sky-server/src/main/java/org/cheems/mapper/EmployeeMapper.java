package org.cheems.mapper;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.cheems.annotation.AutoFill;
import org.cheems.dto.EmployeeLoginDTO;
import org.cheems.dto.EmployeePageQueryDTO;
import org.cheems.entity.Employee;
import org.cheems.enumeration.OperationType;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    @AutoFill(OperationType.INSERT)
    @Insert("insert into employee " +
            "(name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user) " +
            "values " +
            "(#{name},#{username}, #{password}, #{phone}, #{sex}, #{idNumber}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void insert(Employee employee);

    @AutoFill(OperationType.UPDATE)
    @Update("update employee set name = #{name}, username = #{username}, phone = #{phone}, sex =#{sex}, id_number = #{idNumber},update_time = #{updateTime},update_user = #{updateUser} where id = #{id}")
    void update(Employee employee);

    /**
     * @param employeePageQueryDTO
     * @return
     */
    Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    @AutoFill(OperationType.UPDATE)
    @Update("update employee set status = #{status} where id = #{id}")
    void updateStatus(Integer status, Long id);


    @Select("select * from employee where id = #{id}")
    Employee selectEmployeeById(Long id);


}

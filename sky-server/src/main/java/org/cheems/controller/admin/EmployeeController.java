package org.cheems.controller.admin;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.cheems.constant.JwtClaimsConstant;
import org.cheems.dto.EmployeeDTO;
import org.cheems.dto.EmployeeLoginDTO;
import org.cheems.dto.EmployeePageQueryDTO;
import org.cheems.entity.Employee;
import org.cheems.properties.JwtProperties;
import org.cheems.result.PageResult;
import org.cheems.result.Result;
import org.cheems.service.EmployeeService;
import org.cheems.utils.JwtUtil;
import org.cheems.vo.EmployeeLoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工相关接口")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;



    @PostMapping("/login")
    @ApiOperation(value = "员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO){
        log.info("正在登录的员工信息：{}",employeeLoginDTO);
        Employee employee = employeeService.login(employeeLoginDTO);
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID.getValue(), employee.getId());

        String token = JwtUtil.createJWT(jwtProperties.getAdminSecretKey(),jwtProperties.getAdminTtl(),claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .username(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();
        return Result.success(employeeLoginVO);
    }

    @PostMapping("/logout")
    @ApiOperation(value = "员工登出")
    public Result<String> logout(){
        return Result.success();
    }
    @PostMapping
    @ApiOperation(value = "员工注册")
    public Result add(@RequestBody EmployeeDTO employeeDTO){
        log.info("正在新增的员工信息：{}",employeeDTO);
        employeeService.add(employeeDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation(value = "员工分页查询")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("正在分页查询的信息：{}",employeePageQueryDTO);
        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    @PostMapping("/status/{status}")
    @ApiOperation("员工账号状态修改")
    public Result modifyStatus(@PathVariable Integer status, Long id){
        log.info("正在修改员工{}账号状态为：{}",id, status);
        employeeService.updateStatus(status, id);
        return Result.success();
    }

    @PutMapping
    @ApiOperation("员工账号的信息修改")
    public Result modifyEmployeeInfo(@RequestBody EmployeeDTO employeeDTO){
        log.info("正在修改员工账号信息：{}",employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据员工id信息查询")
    public Result<Employee> getById(@PathVariable Long id){
        log.info("正在查询员工的信息，id为：{}",id);
        Employee employee = employeeService.employeeQuery(id);
        return Result.success(employee);
    }



}

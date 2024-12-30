package org.cheems.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.cheems.annotation.AutoFill;
import org.cheems.context.BaseContext;
import org.cheems.enumeration.OperationType;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    /**
     * 切入点
     */
    @Pointcut("execution(* org.cheems.mapper.*.*(..)) && @annotation(org.cheems.annotation.AutoFill)")
    public void autoFillPointCut(){

    }

    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint){
        log.info("注解@AutoFill的前置通知开始.......");

        //1 获取当前方法上的数据库操作类型
        MethodSignature methodSignature  =(MethodSignature)joinPoint.getSignature();
        AutoFill autoFill = methodSignature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = autoFill.value();
        //2 获取被拦截方法的参数  约定实体对象为参数的第一个参数
        Object[] objects = joinPoint.getArgs();

        if (objects == null || objects.length == 0){
            return;
        }
        Object object0 = objects[0];

        //3 准备数据赋值到需要增强的地方，通过反射来赋值
        LocalDateTime now = LocalDateTime.now();
        Long id = BaseContext.getCurrentId();

        if (operationType == OperationType.INSERT){
            try {
                Method setCreateTime = object0.getClass().getDeclaredMethod("setCreateTime", LocalDateTime.class);
                Method setCreateUser = object0.getClass().getDeclaredMethod("setCreateUser", Long.class);
                Method setUpdateTime = object0.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class);
                Method setUpdateUser = object0.getClass().getDeclaredMethod("setUpdateUser", Long.class);
                setCreateTime.invoke(object0, now);
                setCreateUser.invoke(object0, id);
                setUpdateTime.invoke(object0, now);
                setUpdateUser.invoke(object0, id);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }

        }else if (operationType == OperationType.UPDATE){
            try {
                Method setUpdateTime = object0.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class);
                Method setUpdateUser = object0.getClass().getDeclaredMethod("setUpdateUser", Long.class);
                setUpdateTime.invoke(object0, now);
                setUpdateUser.invoke(object0, id);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e ) {
                throw new RuntimeException(e);
            }
        }


    }
}

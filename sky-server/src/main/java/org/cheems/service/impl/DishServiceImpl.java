package org.cheems.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.cheems.annotation.AutoFill;
import org.cheems.constant.MessageConstant;
import org.cheems.dto.DishDTO;
import org.cheems.dto.DishPageQueryDTO;
import org.cheems.entity.Dish;
import org.cheems.entity.DishFlavor;
import org.cheems.enumeration.OperationType;
import org.cheems.exception.DishNameExistedException;
import org.cheems.mapper.DishFlavorMapper;
import org.cheems.mapper.DishMapper;
import org.cheems.result.PageResult;
import org.cheems.service.DishService;
import org.cheems.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import java.util.List;


@Slf4j
@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Override
    @Transactional

    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        // 检测菜品名称是否存在
        String name = dish.getName();
        Dish queryDish = dishMapper.selectByName(name);
        if (queryDish != null) {
            throw new DishNameExistedException(MessageConstant.DISH_NAME_EXISTED);
        }
        dishMapper.insert(dish);
        Long dishId = dish.getId();
        List<DishFlavor> flavors = dishDTO.getFlavors();

        if (flavors != null && !flavors.isEmpty()){
            for (DishFlavor dishFlavor : flavors) {
                dishFlavor.setDishId(dishId);
            }
            dishFlavorMapper.insertBatch(flavors);
        }

    }

    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        return null;
    }

    @Override
    public void deleteBatch(List<Long> ids) {

    }

    @Override
    public DishVO getByIdWithFlavor(Long id) {
        return null;
    }

    @Override
    public void updateWithFlavor(DishDTO dishDTO) {

    }

    @Override
    public void startOrStop(Integer status, Long id) {

    }

    @Override
    public List<Dish> list(Long categoryId) {
        return null;
    }

    @Override
    public List<DishVO> listWithFlavor(Dish dish) {
        return null;
    }
}

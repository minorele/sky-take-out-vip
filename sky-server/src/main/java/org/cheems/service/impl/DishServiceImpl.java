package org.cheems.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.cheems.annotation.AutoFill;
import org.cheems.constant.MessageConstant;
import org.cheems.constant.StatusConstant;
import org.cheems.dto.DishDTO;
import org.cheems.dto.DishPageQueryDTO;
import org.cheems.entity.Dish;
import org.cheems.entity.DishFlavor;
import org.cheems.entity.Employee;
import org.cheems.enumeration.OperationType;
import org.cheems.exception.DeletionNotAllowedException;
import org.cheems.exception.DishNameExistedException;
import org.cheems.mapper.DishFlavorMapper;
import org.cheems.mapper.DishMapper;
import org.cheems.mapper.SetmealDishMapper;
import org.cheems.result.PageResult;
import org.cheems.service.DishService;
import org.cheems.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

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
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());

        Page<DishVO> pages =  dishMapper.pageQuery(dishPageQueryDTO);
        long total = pages.getTotal();
        List<DishVO> records = pages.getResult();
        return new PageResult(total,records);

    }

    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        //条件1：起售中的菜品不能删除
        for (Long id : ids){
            Dish dish = dishMapper.selectById(id);
            if (dish.getStatus() == StatusConstant.ENABLE){
                //起售中
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        //条件2：被套餐关联的菜品不能删除
        List<Long> setmealIds = setmealDishMapper.selectSetmealIdsByDishIds(ids);
        if (setmealIds != null && !setmealIds.isEmpty()){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SET_MEAL);

        }

        //处理1：删除菜品后，关联的口味数据也需要删除掉
        //处理2：可以一次删除一个菜品，也可以批量删除菜品

        //多条sql效率低
//        for (Long id : ids) {
//            dishMapper.deleteById(id);
//            dishFlavorMapper.deleteByDishId(id);
//        }

        //优化成一个sql
        dishMapper.deleteByIds(ids);
        dishFlavorMapper.deleteByDishIds(ids);






    }

    @Override
    public DishVO getByIdWithFlavor(Long id) {
        Dish dish = dishMapper.selectById(id);
        List<DishFlavor> flavors = dishFlavorMapper.selectByDishId(id);
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(flavors);
        return dishVO;
    }

    @Override
    public void updateWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);


        // 修改菜品
        dishMapper.update(dish);

        // 修改口味=先删除再插入
        dishFlavorMapper.deleteByDishId(dish.getId());

        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()){
            for (DishFlavor dishFlavor : flavors) {
                dishFlavor.setDishId(dish.getId());
            }
            dishFlavorMapper.insertBatch(flavors);
        }

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
        List<Dish> dishList = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.selectByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }
}

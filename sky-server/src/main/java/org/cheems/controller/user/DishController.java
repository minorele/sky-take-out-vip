package org.cheems.controller.user;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.cheems.constant.StatusConstant;
import org.cheems.entity.Dish;
import org.cheems.result.Result;
import org.cheems.service.DishService;
import org.cheems.vo.DishVO;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
@Api(tags = "C端-菜品浏览接口")
public class DishController {
    @Autowired
    private DishService dishService;


    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 根据分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<DishVO>> list(Long categoryId) {
        log.info("用户正在查询的分类id下的菜品，分类id是：{}：",categoryId);


        // 查询 redis 缓存的热点数据
        String redisCategoryId = "dish_" + categoryId;
        List<DishVO> list = (List<DishVO>) redisTemplate.opsForValue().get(redisCategoryId);

        // 查询到 直接返回
        if (list != null && !list.isEmpty() ){
            return Result.success(list);
        }

        // 查询不到 数据库查询 并缓存进入redis中

        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);//查询起售中的菜品
        list = dishService.listWithFlavor(dish);
        redisTemplate.opsForValue().set(redisCategoryId, list);



        return Result.success(list);
    }

}

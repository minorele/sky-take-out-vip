package org.cheems.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {


    List<Long> selectSetmealIdsByDishIds(List<Long> dishIds);
}

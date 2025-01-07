package org.cheems.mapper;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;
import org.cheems.annotation.AutoFill;
import org.cheems.dto.DishPageQueryDTO;
import org.cheems.entity.Dish;
import org.cheems.entity.Employee;
import org.cheems.enumeration.OperationType;
import org.cheems.vo.DishVO;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")

    Integer countByCategoryId(Long categoryId);

    @Insert("insert into dish (name, category_id, price, image, description, create_time, update_time, create_user, update_user, status) " +
            "values (#{name}, #{categoryId}, #{price}, #{image}, #{description}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @AutoFill(OperationType.INSERT)
    void insert(Dish dish);

    @Select("select * from dish where name = #{name}")
    Dish selectByName(String name);

    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    @Select("select * from dish where id = #{id}")
    Dish selectById(Long id);
    @Select("select a.* from dish a left join setmeal_dish b on a.id = b.dish_id where b.setmeal_id = #{setmealId}")
    List<Dish> getBySetmealId(Long setmealId);

    @Delete("delete from dish where id = #{id}")
    void deleteById(Long id);

    void deleteByIds(List<Long> ids);

    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);

    List<Dish> list(Dish dish);
}

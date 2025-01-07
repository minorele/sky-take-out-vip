package org.cheems.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.cheems.entity.Orders;

@Mapper
public interface OrderMapper {

    void insert(Orders orders);
}

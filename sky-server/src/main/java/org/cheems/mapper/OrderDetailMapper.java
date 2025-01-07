package org.cheems.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.cheems.entity.OrderDetail;

import java.util.List;

@Mapper
public interface OrderDetailMapper {
    void insertBatch(List<OrderDetail> orderDetailList);
}

package org.cheems.service;

import org.cheems.dto.OrdersSubmitDTO;
import org.cheems.vo.OrderSubmitVO;

public interface OrderService {
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

}

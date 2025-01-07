package org.cheems.service;

import org.cheems.dto.ShoppingCartDTO;
import org.cheems.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {

    void  addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    List<ShoppingCart> showShoppingCart();

    void cleanShoppingCart();

    void subShoppingCart(ShoppingCartDTO shoppingCartDTO);
}

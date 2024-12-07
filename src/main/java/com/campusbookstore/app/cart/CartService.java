package com.campusbookstore.app.cart;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    String viewCart(){
        return "cart/cart";
    }

}

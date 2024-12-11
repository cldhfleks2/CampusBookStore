package com.campusbookstore.app.buy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class BuyController {
    private final BuyRepository buyRepository;
}

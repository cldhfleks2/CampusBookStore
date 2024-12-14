package com.campusbookstore.app.purchase;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PurchaseController {
    private final PurchaseService purchaseService;

    @PostMapping("/order")
    ResponseEntity<String> order(@RequestBody List<PurchaseDTO> purchaseDTOs, Authentication auth) {
        return purchaseService.order(purchaseDTOs, auth);
    }
}

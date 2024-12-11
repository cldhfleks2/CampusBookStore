package com.campusbookstore.app.buy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuyService {
    private final BuyRepository buyRepository;
}

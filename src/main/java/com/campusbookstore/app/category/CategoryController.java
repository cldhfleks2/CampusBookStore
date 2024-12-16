package com.campusbookstore.app.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
}

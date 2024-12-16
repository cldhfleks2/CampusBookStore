package com.campusbookstore.app.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping({"/category", "category/{selectCategoryName}"})
    String viewCategory(@PathVariable(required = false) String selectCategoryName, RedirectAttributes redirectAttributes, Model model) {
        return categoryService.viewCategory(selectCategoryName, redirectAttributes, model);
    }

    //카테고리 이름을 받아서 게시물뷰를 보내줌
    @GetMapping("/categoryView")
    String categoryPostView(String categoryName, Model model) {
        return categoryService.categoryPostView(categoryName, model);
    }
}

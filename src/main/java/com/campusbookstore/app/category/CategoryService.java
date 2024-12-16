package com.campusbookstore.app.category;

import com.campusbookstore.app.error.ErrorService;
import com.campusbookstore.app.post.Post;
import com.campusbookstore.app.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;

    String viewCategory(String selectCategoryName, RedirectAttributes redirectAttributes, Model model) {
        //카테고리 가져오기
        List<Category> categorys = categoryRepository.findAll();
        // 중복되지 않은 category.name만 추출
        List<String> uniqueCategoryNames = categorys.stream()
                .map(Category::getName) // Category 객체에서 name 값만 추출
                .distinct()             // 중복 제거
                .collect(Collectors.toList()); // 결과를 List로 변환
        model.addAttribute("uniqueCategoryNames", uniqueCategoryNames);
        model.addAttribute("selectCategoryName", selectCategoryName); //선택 안됬을때는 빈문자열이 갈거야

        return "category/category";
    }

    //카테고리 이름을 받아서 게시물뷰를 보내줌
    String categoryPostView(String categoryName, Model model) {
        List<Category> categoryList = categoryRepository.findAllByCategoryName(categoryName);
        List<Post> posts = new ArrayList<>();
        for (Category category : categoryList) {
            Long postId = category.getPost().getId();
            Optional<Post> postObj = postRepository.findById(postId);
            if(!postObj.isPresent()) {
                ErrorService.send(HttpStatus.NOT_FOUND.value(), "/categoryView", "게시물을 불러오지 못했습니다.", String.class);
                continue;
            }
            Post post = postObj.get();
            posts.add(post);
        }
        model.addAttribute("posts", posts);

        return "category/category";
    }




}

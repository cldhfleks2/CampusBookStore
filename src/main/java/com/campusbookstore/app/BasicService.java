package com.campusbookstore.app;

import com.campusbookstore.app.category.Category;
import com.campusbookstore.app.category.CategoryRepository;
import com.campusbookstore.app.likey.LikeyRepository;
import com.campusbookstore.app.likey.LikeyService;
import com.campusbookstore.app.post.Post;
import com.campusbookstore.app.post.PostDTO;
import com.campusbookstore.app.post.PostRepository;
import com.campusbookstore.app.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicService {
    private final PostService postService;
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;

    //간단한 뷰
    String viewMain (Model model) {
        //인기 게시물 가져오기
        int popularPostCnt = 8;
        List<Post> popularPosts = postRepository.findTopPostsByLikeyCount(popularPostCnt);
        List<PostDTO> popularPostDTOs = new ArrayList<>();
        for (Post post : popularPosts)
            if(post.getQuantity() > 0)
                popularPostDTOs.add(postService.getPostDTO(post));
        model.addAttribute("popularPostDTOs", popularPostDTOs);

        //최근 게시물 가져오기
        int recentPostCnt = 8;
        List<Post> recentPosts = postRepository.findTopNByStatusOrderByCreateDateDesc(recentPostCnt);
        List<PostDTO> recentPostDTOs = new ArrayList<>();
        for (Post post : recentPosts)
            if(post.getQuantity() > 0)
                recentPostDTOs.add(postService.getPostDTO(post));
        model.addAttribute("recentPostDTOs", recentPostDTOs);

        //카테고리 가져오기
        List<Category> categorys = categoryRepository.findAll();
        // 중복되지 않은 category.name만 추출
        List<String> uniqueCategoryNames = categorys.stream()
                .map(Category::getName) // Category 객체에서 name 값만 추출
                .distinct()             // 중복 제거
                .collect(Collectors.toList()); // 결과를 List로 변환
        model.addAttribute("uniqueCategoryNames", uniqueCategoryNames);
        
        return "main/main";
    }





}

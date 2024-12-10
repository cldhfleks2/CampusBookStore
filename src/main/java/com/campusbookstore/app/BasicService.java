package com.campusbookstore.app;

import com.campusbookstore.app.post.Post;
import com.campusbookstore.app.post.PostDTO;
import com.campusbookstore.app.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicService {
    private final PostService postService;

    //간단한 뷰
    String viewMain (Model model) {
        System.out.println("viewMain");
        //인기 게시물 가져오기


        //최근 게시물 가져오기
        int recentPostCnt = 8;
        List<Post> recentPosts = postService.getRecentPost(recentPostCnt);
        System.out.println(recentPosts.size());
        //DEBUG
        for(Post post : recentPosts) {
            System.out.println(post.getTitle());
        }
        List<PostDTO> recentPostDTOs = new ArrayList<>();
        for (Post post : recentPosts) {
            recentPostDTOs.add(postService.getPostDTO(post));
        }
        System.out.println(recentPostDTOs.size());
        //DEBUG
        for (PostDTO postDTO : recentPostDTOs) {
            System.out.println(postDTO.getTitle());
        }


        model.addAttribute("recentPostDTOs", recentPostDTOs);
        return "main/main";
    }





}

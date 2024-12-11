package com.campusbookstore.app;

import com.campusbookstore.app.likey.LikeyRepository;
import com.campusbookstore.app.likey.LikeyService;
import com.campusbookstore.app.post.Post;
import com.campusbookstore.app.post.PostDTO;
import com.campusbookstore.app.post.PostRepository;
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
    private final PostRepository postRepository;

    //간단한 뷰
    String viewMain (Model model) {
        //인기 게시물 가져오기
        List<Post> popularPosts = postRepository.findPostsByLikeyCount();
        List<PostDTO> popularPostDTOs = new ArrayList<>();
        for (Post post : popularPosts) {
            popularPostDTOs.add(postService.getPostDTO(post));
        }
        model.addAttribute("popularPostDTOs", popularPostDTOs);


        //최근 게시물 가져오기
        int recentPostCnt = 8;
        List<Post> recentPosts = postService.getRecentPost(recentPostCnt);
        List<PostDTO> recentPostDTOs = new ArrayList<>();
        for (Post post : recentPosts) {
            recentPostDTOs.add(postService.getPostDTO(post));
        }

        model.addAttribute("recentPostDTOs", recentPostDTOs);
        return "main/main";
    }





}

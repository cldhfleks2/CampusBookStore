package com.campusbookstore.app.post;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    String viewAddPost () {
        return "post/addPost";
    }
    String viewDetailPost () {
        return "post/detailPost";
    }
    String viewEditPost () {
        return "post/editPost";
    }
    String viewSearch () {
        return "search/search";
    }



    //책 등록
    ResponseEntity<Post> addPost (Post post) {

        return ResponseEntity.ok(post);
    }


    //찜한 리스트 추가 요청
    void addWish(Long postId, Long memberId){

    }

    //헤더의 검색기능
    String searching(String keyword, Model model) {
        //제목과 책 저자로 검색
        List<Post> results = postRepository.findByTitleContainingAndAuthor(keyword, keyword);

        model.addAttribute("keyword", keyword);
        model.addAttribute("posts", results);
        model.addAttribute("postCnt", results.size());
        return "redirect:/search";
    }
}

package com.campusbookstore.app.likey;

import com.campusbookstore.app.post.Post;
import com.campusbookstore.app.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeyService {
    private final LikeyRepository likeyRepository;

    Long getLikeCount(Long postId){
        return (long) likeyRepository.findAllByPostId(postId).size();
    }
}

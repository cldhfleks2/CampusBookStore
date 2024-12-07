package com.campusbookstore.app.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    String viewAddPost () {
        return "post/addPost";
    }

    String viewDetailPost () {
        return "post/detailPost";
    }

    String viewEditPost () {
        return "post/editPost";
    }
}

package com.campusbookstore.app.post;

import com.campusbookstore.app.image.Image;
import com.campusbookstore.app.image.ImageRepository;
import com.campusbookstore.app.member.CustomUserDetail;
import com.campusbookstore.app.member.Member;
import com.campusbookstore.app.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final MemberRepository memberRepository;

    @Value("${file.dir}")
    private String fileDir;
    private String uploadDir = System.getProperty("user.dir") + fileDir;

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
    String addPost (PostDTO postDTO, Authentication auth) throws IOException {
        //Spring SEC로 로그인 정보를 가져옴
        CustomUserDetail userDetail = (CustomUserDetail) auth.getPrincipal();
        String username = userDetail.getUsername();

        //Member객체 가져옴
        Optional<Member> optionalMember = memberRepository.findByName(username);
        Member member;
        if (optionalMember.isPresent()) {
            member = optionalMember.get();
        }else{
            //다시 책등록 폼으로 리턴
            return "redirect:/addPost";
        }

        //Post객체 생성 후 저장
        Post post = new Post();
        post.setTitle(postDTO.getTitle());
        post.setAuthor(postDTO.getAuthor());
        post.setPrice(postDTO.getPrice());
        post.setContent(postDTO.getContent());
        post.setMember(member);
        postRepository.save(post);

        //Image객체 생성 후 저장
        for(MultipartFile file : postDTO.getImages()) {
            //고유값 과 확장자
            String uuid = UUID.randomUUID().toString();
            String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            //폴더가 없으면 생성
            File dir = new File(uploadDir);
            if(!dir.exists()){
                if(dir.mkdir()){
                }
            }
            //저장될 경로를 구함
            String fileName = uuid + ext; //새로 생성된 파일이름
            String filePath = uploadDir + fileName; //저장될 파일 경로(파일이름과 위치)
            //파일을 저장
            file.transferTo(new File(filePath));
            //DB에 저장
            Image image = new Image();
            image.setImagePath(filePath);
            image.setPost(post);
            imageRepository.save(image);
        }

        return "redirect:/main";
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

package com.campusbookstore.app.post;

import com.campusbookstore.app.image.Image;
import com.campusbookstore.app.image.ImageDTO;
import com.campusbookstore.app.image.ImageRepository;
import com.campusbookstore.app.member.AccountDetail;
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
import java.util.ArrayList;
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

    String viewAddPost () {
        return "post/addPost";
    }
    String viewDetailPost (Model model, Authentication auth, Long postId) {
        Optional<Post> postObj = postRepository.findById(postId);
        //없는 postId일때
        if(!postObj.isPresent()) {
            return "error";
        }
        //postDTO생성
        Post post = postObj.get();
        PostDTO postDTO = PostDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .author(post.getAuthor())
                .price(post.getPrice())
                .content(post.getContent())
                .member(post.getMember())
                .build();
        
        //imageDTO생성
        List<Image> images = imageRepository.findByPostId(postId);
        List<ImageDTO> imagesDTO = new ArrayList<>();
        for(Image image : images) {
            ImageDTO imageDTO = ImageDTO.builder()
                    .imagePath(image.getImagePath())
                    .build();
            imagesDTO.add(imageDTO);
        }
        
        //DTO전달
        model.addAttribute("postDTO", postDTO);
        model.addAttribute("imagesDTO", imagesDTO);
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
        AccountDetail userDetail = (AccountDetail) auth.getPrincipal();
        String username = userDetail.getName(); //여기 getUserName에서 바꿈.


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

        //저장경로
        String uploadDir = System.getProperty("user.dir") + fileDir;

        //Image객체 생성 후 저장
        for(MultipartFile file : postDTO.getImages()) {
            //고유 값과 확장자
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
            //String filePath = uploadDir + fileName;
            String filePath = fileName;

            //파일을 저장
            file.transferTo(new File(filePath));

            //DB에 저장
            Image image = new Image();
            image.setImagePath(filePath);
            image.setPost(post);
            imageRepository.save(image);
        }
        System.out.println("성공");

        return "redirect:/main";
    }

    //???찜한 리스트 추가 요청
    ResponseEntity<String> addLike(Long postId, Authentication auth){
    //맴버id는 auth에서 꺼내써라
        return ResponseEntity.ok("찜하기 성공");
    }

    //헤더의 검색 기능
    String searching(String keyword, Model model) {
        //제목과 책 저자로 검색
        List<Post> results = postRepository.findByTitleContainingAndAuthor(keyword, keyword);

        model.addAttribute("keyword", keyword);
        model.addAttribute("posts", results);
        model.addAttribute("postCnt", results.size());
        return "redirect:/search";
    }

    //???장바구니 추가 요청
    ResponseEntity<String> addWish(Long postId, Authentication auth) {
        return ResponseEntity.ok("장바구니 추가 성공");
    }




}

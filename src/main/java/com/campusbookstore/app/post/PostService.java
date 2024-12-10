package com.campusbookstore.app.post;

import com.campusbookstore.app.image.Image;
import com.campusbookstore.app.image.ImageDTO;
import com.campusbookstore.app.image.ImageRepository;
import com.campusbookstore.app.member.AccountDetail;
import com.campusbookstore.app.member.Member;
import com.campusbookstore.app.member.MemberRepository;
import com.campusbookstore.app.member.MemberService;
import com.campusbookstore.app.review.Review;
import com.campusbookstore.app.review.ReviewDTO;
import com.campusbookstore.app.review.ReviewRepository;
import com.campusbookstore.app.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
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
    private final ReviewRepository reviewRepository;
    private final ReviewService reviewService;

    @Value("${file.dir}")
    private String fileDir;

    //DTO를 수정하면 아래 두개 수정해야함.
    //1. Entity -> DTO
    public PostDTO getPostDTO(Post post) {
        if (post == null) return null;
        PostDTO.PostDTOBuilder builder = PostDTO.builder();

        if (post.getId() != null)
            builder.id(post.getId());
        if (post.getTitle() != null && !post.getTitle().isEmpty())
            builder.title(post.getTitle());
        if(post.getAuthor() != null && !post.getAuthor().isEmpty())
            builder.author(post.getAuthor());
        if(post.getPrice() != null && !post.getPrice().isEmpty())
            builder.price(post.getPrice());
        if (post.getContent() != null && !post.getContent().isEmpty())
            builder.content(post.getContent());
        if (post.getMember() != null && post.getMember().getName() != null && !post.getMember().getName().isEmpty())
            builder.author(post.getMember().getName());
        //imagesEntity
        if(post.getImages() != null && !post.getImages().isEmpty())
            builder.imagesEntity(post.getImages());

        return builder.build();
    }

    //2. DTO -> Entity
    public Post convertToPost(PostDTO postDTO) {
        Post post = new Post();

        if (postDTO.getId() != null)
            post.setId(postDTO.getId());
        if (postDTO.getTitle() != null && !postDTO.getTitle().isEmpty()) {
            post.setTitle(postDTO.getTitle());
        }
        if (postDTO.getAuthor() != null && !postDTO.getAuthor().isEmpty())
            post.setAuthor(postDTO.getAuthor());
        if (postDTO.getPrice() != null && !postDTO.getPrice().isEmpty())
            post.setPrice(postDTO.getPrice());
        if (postDTO.getContent() != null && !postDTO.getContent().isEmpty())
            post.setContent(postDTO.getContent());
        if(postDTO.getMember() != null)
            post.setMember(postDTO.getMember());
        if(postDTO.getImages() != null && !postDTO.getImages().isEmpty())
            post.setImages(postDTO.getImagesEntity());

        return post;
    }

    String viewAddPost () {
        return "post/addPost";
    }
    String viewDetailPost (Model model, Authentication auth, Long postId, RedirectAttributes redirectAttributes) {
        Optional<Post> postObj = postRepository.findById(postId);
        //없는 postId일때
        if(!postObj.isPresent()) { return "error"; }
        //삭제된 게시물일때
        if(postObj.get().getStatus() == 0){
            redirectAttributes.addFlashAttribute("alertMessage", "삭제된 게시물입니다.");
            return "redirect:/main";
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

        List<Review> reviews = reviewRepository.findByStatus(1);
        List<ReviewDTO> reviewDTOs = new ArrayList<>();
        for(Review review : reviews) {
            reviewDTOs.add(reviewService.getReviewDTO(review));
        }


        //DTO전달
        model.addAttribute("postDTOs", postDTO);
        model.addAttribute("imagesDTOs", imagesDTO);
        model.addAttribute("reviewDTOs", reviewDTOs);
        return "post/detailPost";
    }
    String viewEditPost () {
        return "post/editPost";
    }
    String viewSearch () {
        return "search/search";
    }


    //책 등록
    String addPost (PostDTO postDTO, Authentication auth) throws Exception {
        //Spring SEC로 로그인 정보를 가져옴
        AccountDetail userDetail = (AccountDetail) auth.getPrincipal();
        String name = userDetail.getName(); //getUserName에서 바꿈.


        //Member객체 가져옴
        Optional<Member> optionalMember = memberRepository.findByName(name);
        if (!optionalMember.isPresent()) { return "redirect:/addPost"; }
        Member member = optionalMember.get();

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
            String filePath = uploadDir + fileName;

            //파일을 저장
            file.transferTo(new File(filePath));

            //DB에 저장
            Image image = new Image();
//            image.setImagePath(filePath);
            image.setImagePath(fileName);
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
    //인기 게시물 보여주기


    //최근 게시물 보여주기
    public List<Post> getRecentPost(int n){
        return postRepository.findTopNByStatusOrderByCreateDateDesc(n);
    }



}

package com.campusbookstore.app.post;

import com.campusbookstore.app.error.ErrorService;
import com.campusbookstore.app.image.Image;
import com.campusbookstore.app.image.ImageDTO;
import com.campusbookstore.app.image.ImageRepository;
import com.campusbookstore.app.image.ImageService;
import com.campusbookstore.app.likey.Likey;
import com.campusbookstore.app.likey.LikeyRepository;
import com.campusbookstore.app.member.AccountDetail;
import com.campusbookstore.app.member.Member;
import com.campusbookstore.app.member.MemberRepository;
import com.campusbookstore.app.review.Review;
import com.campusbookstore.app.review.ReviewDTO;
import com.campusbookstore.app.review.ReviewRepository;
import com.campusbookstore.app.review.ReviewService;
import com.campusbookstore.app.wish.Wish;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
    private final LikeyRepository likeyRepository;
    private final ReviewService reviewService;
    private final ImageService imageService;


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
            builder.member(post.getMember());
        //imagesEntity
        if(post.getImages() != null && !post.getImages().isEmpty())
            builder.imagesEntity(post.getImages());
        if (post.getCreateDate() != null)
            builder.createDate(post.getCreateDate()); // createDate 추가
        if (post.getQuantity() != null)
            builder.quantity(post.getQuantity());

        return builder.build();
    }

    //2. DTO -> Entity
    public Post convertToPost(PostDTO postDTO) {
        if (postDTO == null) return null;
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
        if (postDTO.getCreateDate() != null)
            post.setCreateDate(postDTO.getCreateDate());
        if (postDTO.getQuantity() != null)
            post.setQuantity(postDTO.getQuantity());

        return post;
    }

    String viewAddPost () {
        return "post/addPost";
    }
    String viewDetailPost (Model model, Authentication auth, Long postId, RedirectAttributes redirectAttributes) {
        Optional<Post> postObj = postRepository.findById(postId);
        //없는 postId일때
        if(!postObj.isPresent()) {
            return ErrorService.send(
                    HttpStatus.NOT_FOUND.value(),
                    "/detailPost/{postId}",
                    "DB에 없는 게시물",
                    model
            );
        }
        //삭제된 게시물일때
        if(postObj.get().getStatus() == 0){
            redirectAttributes.addFlashAttribute("alertMessage", "삭제된 게시물입니다.");
            return "redirect:/main";
        }

        //postDTO생성
        Post post = postObj.get();
        PostDTO postDTO = getPostDTO(post);
        
        //imageDTO생성
        List<Image> images = imageRepository.findByPostId(postId);
        List<ImageDTO> imagesDTO = new ArrayList<>();
        for(Image image : images) {
            imagesDTO.add(imageService.getImageDTO(image));
        }
        
        //reviewDTO생성
        List<Review> reviews = reviewRepository.findAllByStatus();
        List<ReviewDTO> reviewDTOs = new ArrayList<>();
        for(Review review : reviews) {
            reviewDTOs.add(reviewService.getReviewDTO(review));
        }
        
        //like갯수세기
        List<Likey> likeys = likeyRepository.findAllByPostIdAndStatus(postId);

        //내가 좋아요를 눌렀는지 확인
        //note: 2개이상 객체가 존재하면 에러임
        List<Likey> iamlikeyObj = likeyRepository.findByPostIdAndMemberName(postId, auth.getName());
        String iamlikey = "0"; //처음엔 좋아요 안누름
        if(!iamlikeyObj.isEmpty() && iamlikeyObj.get(0).getStatus() == 1) { //likey객체가 존재하고 좋아요를 눌렀을때
            iamlikey = "1";
        }

        //DTO전달
        model.addAttribute("post", postDTO);
        model.addAttribute("imagesDTOs", imagesDTO);
        model.addAttribute("reviewDTOs", reviewDTOs);
        model.addAttribute("likeyCnt", likeys.size());
        model.addAttribute("iamlikey", iamlikey); //0: 안좋아요   1: 좋아요
        return "post/detailPost";
    }
    String viewEditPost () {
        return "post/editPost";
    }
    String viewSearch () {
        return "search/search";
    }
    //책 등록
    String addPost (PostDTO postDTO, Authentication auth, Model model) throws Exception {
        //Spring SEC로 로그인 정보를 가져옴
        AccountDetail userDetail = (AccountDetail) auth.getPrincipal();
        String name = userDetail.getName(); //getUserName에서 바꿈.

        //Member객체 가져옴
        Optional<Member> optionalMember = memberRepository.findByName(name);
        if (!optionalMember.isPresent()) {
            return ErrorService.send(
                    HttpStatus.UNAUTHORIZED.value(),
                    "/addPost",
                    "DB없는 회원",
                    model
            );
        }
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
            image.setImagePath(fileName);
            image.setPost(post);
            imageRepository.save(image);
        }

        return "redirect:/main";
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





}

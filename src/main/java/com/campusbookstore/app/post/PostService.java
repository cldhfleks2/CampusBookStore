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
import com.campusbookstore.app.report.ReportPost;
import com.campusbookstore.app.report.ReportPostRepository;
import com.campusbookstore.app.report.ReportReview;
import com.campusbookstore.app.report.ReportReviewRepository;
import com.campusbookstore.app.review.Review;
import com.campusbookstore.app.review.ReviewDTO;
import com.campusbookstore.app.review.ReviewRepository;
import com.campusbookstore.app.review.ReviewService;
import com.campusbookstore.app.wish.Wish;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
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
    private final ReportPostRepository reportPostRepository;
    private final ReportReviewRepository reportReviewRepository;

    @Value("${file.dir}")
    private String fileDir;

    //DTO를 수정하면 아래 두개 수정해야함.
    //1. Entity -> DTO
    @Transactional
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
    @Transactional
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
                    String.class
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
    String viewEditPost (Long postId, Authentication auth, Model model) {
        //유효성 검사
        Optional<Post> postObj = postRepository.findById(postId);
        if(!postObj.isPresent()) return ErrorService.send(HttpStatus.NOT_FOUND.value(), "/editPost", "존재하지않는 게시물입니다.", String.class);
        Optional<Member> memberObj = memberRepository.findByName(auth.getName());
        if(!memberObj.isPresent()) return ErrorService.send(HttpStatus.UNAUTHORIZED.value(), "/editPost", "사용자 정보가 없습니다", String.class);
        Post post = postObj.get();
        Member member = memberObj.get();
        //본인인지 확인
        if(post.getMember().getId() != member.getId()) return ErrorService.send(HttpStatus.FORBIDDEN.value(), "/editPost", "본인이 아닙니다.", String.class);


        model.addAttribute("post", post);

        return "post/editPost";
    }
    String viewSearch () {
        return "search/search";
    }

    @Transactional
    void savePostAndImages(PostDTO postDTO, Member member) throws Exception {
        //DB수정
        Post post = convertToPost(postDTO);
        post.setMember(member);
        postRepository.save(post);

        //저장경로
        String uploadDir = System.getProperty("user.dir") + fileDir;

        //Image객체 생성 후 저장
        if(postDTO.getImages() != null){
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
        }
    }
    //책 등록
    @Transactional
    String addPost (PostDTO postDTO, Authentication auth) throws Exception {
        //Spring SEC로 로그인 정보를 가져옴
        String name = auth.getName();

        //Member객체 가져옴
        Optional<Member> memberObj = memberRepository.findByName(name);
        if (!memberObj.isPresent()) {
            return ErrorService.send(
                    HttpStatus.UNAUTHORIZED.value(),
                    "/addPost",
                    "사용자 정보가 존재 하지 않습니다.",
                    String.class
            );
        }
        //DB에 게시물 저장
        Member member = memberObj.get();
        savePostAndImages(postDTO, member);

        return "redirect:/main";
    }
    //헤더의 검색 기능
    @Transactional
    String searching(String keyword, Model model) {
        //제목과 책 저자로 검색
        List<Post> results = postRepository.findByTitleContainingAndAuthor(keyword, keyword);

        model.addAttribute("keyword", keyword);
        model.addAttribute("posts", results);
        model.addAttribute("postCnt", results.size());
        return "redirect:/search";
    }
    //책 수정 내용 DB에 수정
    @Transactional
    ResponseEntity<String> editPost(PostDTO postDTO, Authentication auth) throws Exception {
        Optional<Post> postObj = postRepository.findById(postDTO.getId());
        Optional<Member> memberObj = memberRepository.findByName(auth.getName());
        //유효성검사
        //원래 존재하던 게시물인지
        if(!postObj.isPresent()) return ErrorService.send(HttpStatus.NOT_FOUND.value(), "/editPost", "존재 하지 않는 게시물을 수정할 수 없습니다.", ResponseEntity.class);
        //사용자가 존재 하는지
        if(!memberObj.isPresent()) return ErrorService.send(HttpStatus.UNAUTHORIZED.value(), "/editPost", "사용자 정보가 존재하지 않습니다.", ResponseEntity.class);
        //본인 게시물인지 - 이거하면 에러나서 안돼
//        if(postDTO.getMember().getId() != memberObj.get().getId()) return ErrorService.send(HttpStatus.FORBIDDEN.value(), "/editPost", "본인의 게시물만 수정 할 수 있습니다.", ResponseEntity.class);

        //DB수정
        Member member = memberObj.get();
        savePostAndImages(postDTO, member);

        return ResponseEntity.status(HttpStatus.OK.value()).body("책 정보 수정 성공");
    }

    //TODO 게시물 삭제
    @Transactional
    ResponseEntity<String> deletePost (PostDTO postDTO, Authentication auth) {
        System.out.println("시작");
        System.out.println(postDTO.getId());
        Optional<Post> postObj = postRepository.findById(postDTO.getId());
        System.out.println("1");
        Optional<Member> memberObj = memberRepository.findByName(auth.getName());
        //유효성검사
        //원래 존재하던 게시물인지
        if(!postObj.isPresent()) return ErrorService.send(HttpStatus.NOT_FOUND.value(), "/deletePost", "존재 하지 않는 게시물을 수정할 수 없습니다.", ResponseEntity.class);
        System.out.println("2");
        //사용자가 존재 하는지
        if(!memberObj.isPresent()) return ErrorService.send(HttpStatus.UNAUTHORIZED.value(), "/deletePost", "사용자 정보가 존재하지 않습니다.", ResponseEntity.class);
        System.out.println("3");
        //본인 게시물인지 - 이거하면 에러나서 안돼
//        if(postDTO.getMember().getId() != memberObj.get().getId()) return ErrorService.send(HttpStatus.FORBIDDEN.value(), "/deletePost", "본인의 게시물만 삭제 할 수 있습니다.", ResponseEntity.class);

        System.out.println("여기까지 와야 성공");
        postRepository.delete(postObj.get());
        System.out.println("찐 성공");
        return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).build();
    }

    //TODO 게시물 신고
    ResponseEntity<String> reportPost(Long postId, Boolean inappropriateContent, Boolean spamOrAds, Boolean copyrightInfringement, Boolean misinformation, String otherReason, Authentication auth) {
        //유효성 검사
        //1. 게시물 존재 여부
        Optional<Post> postObj = postRepository.findById(postId);
        if(!postObj.isPresent()) ErrorService.send(HttpStatus.NOT_FOUND.value(), "/reportPost", "게시물이 존재 하지 않습니다.", ResponseEntity.class);
        //2. 사용자 존재 여부
        Optional<Member> memberObj = memberRepository.findByName(auth.getName());
        if(!memberObj.isPresent()) ErrorService.send(HttpStatus.UNAUTHORIZED.value(), "/reportPost", "사용자 정보가 존재하지 않습니다.", ResponseEntity.class);
        Post post = postObj.get();
        Member member = memberObj.get();
        List<ReportPost> reportPostObj = reportPostRepository.findByMemberNameAndPostId(auth.getName(), postId);
        //3. 이미 신고한 게시물의 경우 제외 : DB에존재하고 status=1인것.
        if(!reportPostObj.isEmpty() && reportPostObj.get(0).getStatus() == 1)
            return ErrorService.send(HttpStatus.ALREADY_REPORTED.value(), "/reportPost", "이미 신고한 게시물 입니다.", ResponseEntity.class);

        //객체 생성
        ReportPost reportPost = new ReportPost();
        reportPost.setInappropriateContent(inappropriateContent);
        reportPost.setSpamOrAds(spamOrAds);
        reportPost.setCopyrightInfringement(copyrightInfringement);
        reportPost.setMisinformation(misinformation);
        reportPost.setOtherReason(otherReason);
        reportPost.setPost(post);
        reportPost.setMember(member);

        //DB저장
        reportPostRepository.save(reportPost);
        return ResponseEntity.status(HttpStatus.ACCEPTED.value()).build();
    }


    //TODO 리뷰 신고
    ResponseEntity<String> reportReview(Long reviewId, Boolean inappropriateContent, Boolean spamOrAds, Boolean copyrightInfringement,
                                      Boolean misinformation, String otherReason, Authentication auth) {
        //유효성 검사
        //1. 게시물 존재 여부
        Optional<Review> reviewObj = reviewRepository.findById(reviewId);
        if(!reviewObj.isPresent()) ErrorService.send(HttpStatus.NOT_FOUND.value(), "/reportReview", "리뷰가 존재 하지 않습니다.", ResponseEntity.class);
        //2. 사용자 존재 여부
        Optional<Member> memberObj = memberRepository.findByName(auth.getName());
        if(!memberObj.isPresent()) ErrorService.send(HttpStatus.UNAUTHORIZED.value(), "/reportReview", "사용자 정보가 존재하지 않습니다.", ResponseEntity.class);
        Review review = reviewObj.get();
        Member member = memberObj.get();
        List<ReportReview> reportReviewObj = reportReviewRepository.findByMemberNameAndReviewId(auth.getName(), reviewId);
        //3. 이미 신고한 게시물의 경우 제외 : DB에존재하고 status=1인것.
        if(!reportReviewObj.isEmpty() && reportReviewObj.get(0).getStatus() == 1)
            return ErrorService.send(HttpStatus.ALREADY_REPORTED.value(), "/reportReview", "이미 신고한 리뷰 입니다.", ResponseEntity.class);

        //객체 생성
        ReportReview reportReview = new ReportReview();
        reportReview.setInappropriateContent(inappropriateContent);
        reportReview.setSpamOrAds(spamOrAds);
        reportReview.setCopyrightInfringement(copyrightInfringement);
        reportReview.setMisinformation(misinformation);
        reportReview.setOtherReason(otherReason);
        reportReview.setReview(review);
        reportReview.setMember(member);

        //DB저장
        reportReviewRepository.save(reportReview);
        return ResponseEntity.status(HttpStatus.ACCEPTED.value()).build();
    }
}

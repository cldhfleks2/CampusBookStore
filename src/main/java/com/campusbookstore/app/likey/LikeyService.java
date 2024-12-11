package com.campusbookstore.app.likey;

import com.campusbookstore.app.member.Member;
import com.campusbookstore.app.member.MemberRepository;
import com.campusbookstore.app.post.Post;
import com.campusbookstore.app.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeyService {
    private final LikeyRepository likeyRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    Long getLikeCount(Long postId){
        return (long) likeyRepository.findAllByPostId(postId).size();
    }

    //찜한 리스트 추가 요청
    ResponseEntity<String> addLikey(Long postId, Authentication auth){
        Optional<Post> postObj = postRepository.findById(postId);
        //게시물이 없는경우 에러
        if(!postObj.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("게시물이 없음");
        Post post = postObj.get();
        Optional<Member> memberObj = memberRepository.findByName(auth.getName());
        //내 로그인한 정보가 없으면 오류
        if(!memberObj.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("본인 정보가 존재하지 않음.");
        Member member = memberObj.get();
        //본인 게시물이라면 제외
        if(post.getMember().getName().equals(auth.getName())) 
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("isyou");
        //이전에 좋아요를 했는지 확인
        // note : List가 2개이상 존재 한다면 에러임..
        List<Likey> likeyObj = likeyRepository.findByPostIdAndMemberName(postId, auth.getName());
        if(likeyObj.isEmpty()) { //좋아요 기록이 없으면 DB에 최초 생성
            Likey likey = new Likey();
            likey.setPost(post);
            likey.setMember(member);
            likeyRepository.save(likey);
            return ResponseEntity.status(HttpStatus.OK).body("likey"); //"likey" 전달
        }else{ //있었으면 status확인
            Likey likey = likeyObj.get(0); //가장 첫번째 요소일거니까..
            Integer status = (likey.getStatus() + 1) % 2; //status toggle
            String data[] = {"noLikey", "likey"};
            likey.setStatus(status);
            likeyRepository.save(likey); //바뀐 status 수정
            return ResponseEntity.status(HttpStatus.OK).body(data[status]); //"noLikey", "likey"  전달
        }


    }
}

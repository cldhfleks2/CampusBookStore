package com.campusbookstore.app.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByName(String name);

    //status=1 인것만 가져오는 JPA메소드
    List<Member> findByStatus(int status);
}

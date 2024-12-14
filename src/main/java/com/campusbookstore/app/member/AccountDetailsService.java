package com.campusbookstore.app.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final RoleService roleService;

    //유저 정보를 전달해주는 서비스
    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Optional<Member> member = memberRepository.findByName(name);

        if(member.isEmpty())
            throw new UsernameNotFoundException("user not found");

        var loginMember = member.get();
        List<GrantedAuthority> authorities = new ArrayList<>();

        //여기서 멤버들의 등급을 정함
        String role = roleService.getUserRole(loginMember.getName()).getRoleName();
        authorities.add(new SimpleGrantedAuthority(role));

        //유저 정보 리턴
        AccountDetail realMember = new AccountDetail(loginMember.getName(), loginMember.getPassword(), authorities);
        realMember.setName(loginMember.getName());
        //등급도 전달하면 안되나..?
        realMember.setRole(role);
        return realMember;
    }
}


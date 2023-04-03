package com.example.encrypt.service;

import com.example.encrypt.domain.Member;
import com.example.encrypt.dto.req.AddMemberDto;
import com.example.encrypt.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public ResponseEntity<?> addMember(AddMemberDto addMemberDto) {
        Member member = Member.builder()
                .id(addMemberDto.getId())
                .password(passwordEncoder.encode(addMemberDto.getPassword()))
                .phoneNumber(addMemberDto.getPhoneNumber())
                .username(addMemberDto.getUsername())
                .address(addMemberDto.getAddress())
                .build();

        memberRepository.save(member);

        return ResponseEntity.ok("OK");
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> getMemberInfo(Long seq) {
        return ResponseEntity.ok(memberRepository.findById(seq));
    }
}

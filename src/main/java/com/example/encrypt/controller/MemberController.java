package com.example.encrypt.controller;

import com.example.encrypt.dto.req.AddMemberDto;
import com.example.encrypt.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/member")
    public ResponseEntity<?> addMember(@RequestBody AddMemberDto addMemberDto){
        return memberService.addMember(addMemberDto);

    }

    @GetMapping("/member/{seq}")
    public ResponseEntity<?> getMemberInfo(@PathVariable Long seq){
        return memberService.getMemberInfo(seq);

    }

    @PostMapping("/member/enc")
    public ResponseEntity<?> getEncryptMemberInfo(HttpServletRequest request ,@RequestBody AddMemberDto dto) throws IOException {

        ServletInputStream inputStream = request.getInputStream();
        byte[] bytes = inputStream.readAllBytes();
        String s = new String(bytes, StandardCharsets.UTF_8);
        System.out.println("s = " + s);
        System.out.println("id : "+dto.getId());
        System.out.println("password : "+dto.getPassword());
        System.out.println("username : "+dto.getUsername());
        System.out.println("address : "+dto.getAddress());
        System.out.println("phoneNumber : "+dto.getPhoneNumber());

        memberService.addMember(dto);

        return ResponseEntity.ok(dto);
    }
}

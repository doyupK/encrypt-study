package com.example.encrypt.controller;

import com.example.encrypt.dto.req.AddMemberDto;
import com.example.encrypt.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}

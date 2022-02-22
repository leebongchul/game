package com.game.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.game.domain.MemberDTO;
import com.game.service.MemberService;

@Controller
@RequestMapping("/mypage/*")
public class MypageController {
    
    @Autowired
    private MemberService memberService;
    
    @GetMapping(value = "/mypagemain")
    public String mypagemain(@SessionAttribute(name = "loginMem", required = false) MemberDTO loginMember,
            Model model) {
        if(loginMember == null) {
            return "member/index";
        }
        return "mypage/mypagemain";
    }
    
    

    @GetMapping(value = "/userupdate")
    public String userupdatefunction(@SessionAttribute(name = "loginMem", required = false) MemberDTO loginMember,
            Model model) {
        System.out.println("사용자정보 접속");
        MemberDTO user = new MemberDTO();
        user.setMemId("aaaaaa1");
        user = memberService.selectMember(user);
        model.addAttribute("user",user);
        return "mypage/userupdate";
    }
    
    // 닉네임 중복검사
    @PostMapping(value = "/mypage/nickcheck.do")
    @ResponseBody
    public int JoinNickCheck(@RequestParam String memNick) {
        System.out.println("nickcheck");
        System.out.println(memNick);
        return memberService.memberNickcheck(memNick);
    }

    // 핸드폰번호 중복검사
    @PostMapping(value = "/mypage/hpcheck.do")
    @ResponseBody
    public int JoinHpCheck(@RequestParam String memHp) {

        return memberService.memberHpcheck(memHp);
    }

    // 이메일 중복검사
    @PostMapping(value = "/mypage/emailcheck.do")
    @ResponseBody
    public int JoinEmailCheck(@RequestParam String memEmail) {

        return memberService.memberEmailcheck(memEmail);
    }
    
}

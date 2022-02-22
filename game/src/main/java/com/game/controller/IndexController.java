package com.game.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.game.domain.MemberDTO;

@Controller
@RequestMapping("")
public class IndexController {

    @GetMapping(value = "/index")
    public String indexfunction(@SessionAttribute(name = "loginMem", required = false) MemberDTO loginMember,
            Model model) {

        model.addAttribute("", loginMember);
        return "index.html";
    }

}

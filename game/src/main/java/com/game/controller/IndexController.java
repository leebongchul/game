package com.game.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.game.Util.UiUtils;
import com.game.constant.Method;
import com.game.domain.MemberDTO;

@Controller
@RequestMapping("/*")
public class IndexController extends UiUtils {

	@GetMapping(value = "/index")
	public String indexfunction(@SessionAttribute(name = "loginMem", required = false) MemberDTO loginMember,
			Model model) {
	    model.addAttribute("headersession", loginMember);
		model.addAttribute("member", loginMember);
		return "index.html";
	}

	/** 로그아웃 **/
	@GetMapping("/logout.do")
	public String logout(HttpServletRequest request, Model model) {

		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate(); // 세션 날림
		}

		return showMessageWithRedirect("로그아웃되었습니다.", "../index", Method.GET, null, model);
	}

	@GetMapping(value = "/header")
	public String headerfunction(@SessionAttribute(name = "loginMem", required = false) MemberDTO loginMember,
			Model model) {

		model.addAttribute("session", loginMember);
		return "header.html";
	}

}

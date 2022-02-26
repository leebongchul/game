package com.game.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.game.Util.UiUtils;
import com.game.constant.Method;
import com.game.domain.BoardDTO;
import com.game.domain.MemberDTO;
import com.game.service.MemberService;
import com.game.service.MemberServiceImpl;

@Controller
@RequestMapping("/member/*")
public class MemberController extends UiUtils {

	@Autowired
	private MemberService memberService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping(value = "/index")
	public String indexfunction(@SessionAttribute(name = "loginMem", required = false) MemberDTO loginMember,
			Model model) {

		model.addAttribute("member", loginMember);
		return "../index";
	}

	/****************** GET *************************/
	@GetMapping(value = "/test")
	public String testfunction(@SessionAttribute(name = "loginMem", required = false) MemberDTO loginMember,
			Model model) {
		// 세션에 회원 데이터가 없으면 홈으로 이동
		if (loginMember == null) {
			return "member/login";
		}

		// 세션이 유지되면 로그인 홈으로 이동
		model.addAttribute("member", loginMember);
		return "member/test";
	}
	/*
	 * @GetMapping(value = "/join") public String joinfunction(Model model) { return
	 * "member/join"; }
	 */

	@GetMapping(value = "/join")
	public String joinfunction(@ModelAttribute("params") MemberDTO params, Model model) {
		if (params.getMemId() == null) {
			model.addAttribute("member", new MemberDTO());
		}
		return "member/join";
	}

	/** 카카오톡 로그인으로 회원가입 **/
	@PostMapping(value = "/join")
	public String joinfunction(@ModelAttribute("params") BoardDTO params, Model model) {
		if (params.getMemId() == null) {
			model.addAttribute("member", new BoardDTO());
		} else {
			if (params.getMemEmail() != null) {
				String[] email = params.getMemEmail().split("@");
				String email1 = email[0];
				String email2 = email[1];
				model.addAttribute("memEmail1", email1);
				model.addAttribute("memEmail2", email2);
			}
			model.addAttribute("member", params);
		}

		return "member/join";
	}

	@GetMapping(value = "/kakaologin")
	public String OpenKakaoLogin(@ModelAttribute("params") MemberDTO params, Model model, HttpServletRequest request) {

		if (params.getMemId() == null) {
			model.addAttribute("member", new BoardDTO());
			return showMessageWithRedirect("카카오톡 로그인이 정상적으로 이루어지지 않았습니다.", "/member/login", Method.GET, null, model);
		} else {
			try {
				MemberDTO result = memberService.selectMember(params);

				if (result == null) {
					HashMap<String, Object> map = new HashMap<String, Object>();

					map.put("memId", params.getMemId());
					map.put("memNick", params.getMemNick());
					if (params.getMemEmail() != null)
						map.put("memEmail", params.getMemEmail());
					if (params.getMemGender() != null)
						map.put("memGender", params.getMemGender());

					return showMessageWithRedirect("등록된 회원정보가 존재하지 않습니다. 회원가입창으로 이동합니다.", "/member/join", Method.POST,
							map, model);
				}
				// 아이디와 비밀번호가 일치
				HttpSession session = request.getSession(); // 세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성하여 반환
				session.setAttribute("loginMem", result); // 세션에 로그인 회원 정보 보관

				return showMessageWithRedirect("로그인이 완료되었습니다.", "/member/test", Method.GET, null, model);

			} catch (DataAccessException e) {
				return showMessageWithRedirect("데이터베이스 처리 과정에 문제가 발생하였습니다.", "/member/login", Method.GET, null, model);

			} catch (Exception e) {
				return showMessageWithRedirect("시스템에 문제가 발생하였습니다.", "/member/login", Method.GET, null, model);
			}

		}
	}

	@GetMapping(value = "/userdelete")
	public String userdeletefunction(Model model) {
		return "member/userdelete";
	}

	@GetMapping(value = "/mypage")
	public String mypagefunction(Model model) {
		return "member/mypage";
	}

	@GetMapping(value = "/adminpage")
	public String adminpagefunction(Model model) {
		return "member/adminpage";
	}

	@GetMapping(value = "/login")
	public String OpenLoginpage(Model model) {
		return "member/login";
	}

	@GetMapping(value = "/idfind")
	public String idfindfunction(Model model) {
		return "member/idfind";
	}

	@GetMapping(value = "/passfind")
	public String passfindfunction(Model model) {
		return "member/passfind";
	}

	@GetMapping(value = "/newpass")
	public String newpassfunction(@SessionAttribute(name = "loginMem", required = false) MemberDTO loginMember,
			MemberDTO member, Model model) {
		if (loginMember == null) {
			model.addAttribute("member", member);
			return "member/newpass";
		}
		model.addAttribute("member", loginMember);
		return "member/newpass";

	}

	/********** 중복 체크 ********************************/
	// 아이디 중복검사
	@PostMapping(value = "/member/idcheck.do")
	@ResponseBody
	public int JoinIdCheck(@RequestParam String memId) {

		return memberService.memberIdcheck(memId);
	}

	// 닉네임 중복검사
	@PostMapping(value = "/member/nickcheck.do")
	@ResponseBody
	public int JoinNickCheck(@RequestParam String memNick) {

		return memberService.memberNickcheck(memNick);
	}

	// 핸드폰번호 중복검사
	@PostMapping(value = "/member/hpcheck.do")
	@ResponseBody
	public int JoinHpCheck(@RequestParam String memHp) {

		return memberService.memberHpcheck(memHp);
	}

	// 이메일 중복검사
	@PostMapping(value = "/member/emailcheck.do")
	@ResponseBody
	public int JoinEmailCheck(@RequestParam String memEmail) {

		return memberService.memberEmailcheck(memEmail);
	}

	/***************************************************/

	/****************** POST *************************/

	/** 차단회원 로그인 차단 **/
	
	/** 회원가입 **/
	@PostMapping(value = "/member/insert.do")
	public String memberjoin(MemberDTO member, Model model) {
		try {
			member.setMemPass(passwordEncoder.encode(member.getMemPass()));
			boolean joinResult = memberService.insertMember(member);
			if (joinResult == false) {
				return showMessageWithRedirect("회원가입에 실패하였습니다.", "/member/join", Method.GET, null, model);
			}
		} catch (DataAccessException e) {
			return showMessageWithRedirect("데이터베이스 처리 과정에 문제가 발생하였습니다.", "/member/join", Method.GET, null, model);
		} catch (Exception e) {
			return showMessageWithRedirect("시스템에 문제가 발생하였습니다.", "/member/join", Method.GET, null, model);
		}
		return showMessageWithRedirect("회원가입이 완료되었습니다.", "/member/login", Method.GET, null, model);
	}

	/** 아이디찾기 **/
	@PostMapping(value = "/idfind")
	public String idFindCheck(MemberDTO member, Model model) {
		String findid = "";
		try {
			boolean idfindresult = memberService.idfindMember(member);
			findid = memberService.idfind(member);
			if (idfindresult == false) {
				return showMessageWithRedirect("아이디찾기에 실패하였습니다.", "/member/idfind", Method.GET, null, model);

			}
		} catch (DataAccessException e) {
			return showMessageWithRedirect("데이터베이스 처리 과정에 문제가 발생하였습니다.", "/member/login", Method.GET, null, model);

		} catch (Exception e) {
			return showMessageWithRedirect("시스템에 문제가 발생하였습니다.", "/member/login", Method.GET, null, model);
		}

		return showMessageWithRedirect("아이디는" + findid + "입니다.", "/member/login", Method.GET, null, model);

	}

	/** 비밀번호 재설정 **/
	@PostMapping(value = "/newpass")
	public String newPassCheck(MemberDTO member, Model model) {
		System.out.println("아이디값" + member.getMemId());
		try {
		    
			member.setMemPass(passwordEncoder.encode(member.getMemPass()));
			int newpass = memberService.newpassMember(member);
			if (newpass == 0) {
				return showMessageWithRedirect("비밀번호 재설정에 실패하였습니다.", "/member/newpass", Method.GET, null, model);
			}
		} catch (DataAccessException e) {
			return showMessageWithRedirect("데이터베이스 처리 과정에 문제가 발생하였습니다.", "/member/login", Method.GET, null, model);

		} catch (Exception e) {
			return showMessageWithRedirect("시스템에 문제가 발생하였습니다.", "/member/login", Method.GET, null, model);
		}

		return showMessageWithRedirect("비밀번호 재설정에 성공하였습니다.", "/member/login", Method.GET, null, model);
	}

	/** 비밀번호찾기 **/
	@PostMapping(value = "/passfind")
	public String passFindCheck(MemberDTO member, Model model) {
		try {
			int passfindresult = memberService.passfindMember(member);
			if (passfindresult == 0) {
				return showMessageWithRedirect("비밀번호찾기에 실패하였습니다.", "/member/passfind", Method.GET, null, model);

			}
		} catch (DataAccessException e) {
			return showMessageWithRedirect("데이터베이스 처리 과정에 문제가 발생하였습니다.", "/member/login", Method.GET, null, model);

		} catch (Exception e) {
			return showMessageWithRedirect("시스템에 문제가 발생하였습니다.", "/member/login", Method.GET, null, model);
		}

		return showMessageWithRedirect("확인되었습니다. 비밀번호 재설정창으로 넘어갑니다.", "/member/newpass?memId=" + member.getMemId(),
				Method.GET, null, model);

	}

	/** 로그인 **/
	@PostMapping(value = "/login")
	public String successLogin(MemberDTO member, Model model, HttpServletRequest request) {
		try {
			MemberDTO result = memberService.selectMember(member);
			String sememid = memberService.selectMember(member).getMemId();
			String sememblock = memberService.selectMember(member).getMemBlock();
			String sememblockdate = memberService.selectMember(member).getMemBlockDate();
			String sememblockend = memberService.selectMember(member).getMemBlockEndDate();
			if (result.getMemId() == null) {
				return showMessageWithRedirect("해당 아이디가 존재하지 않습니다.", "/member/login", Method.GET, null, model);
			}

			if (!passwordEncoder.matches(member.getMemPass(), result.getMemPass())) {
				return showMessageWithRedirect("비밀번호가 일치하지 않습니다.", "/member/login", Method.GET, null, model);
			}
			
			HttpSession session = request.getSession(); // 세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성하여 반환
			session.setAttribute("loginMem", result); // 세션에 로그인 회원 정보 보관
			session.setAttribute("sememid", sememid);
			session.setAttribute("sememblock", sememblock);
			session.setAttribute("sememblockdate", sememblockdate);
			session.setAttribute("sememblockend",sememblockend);
//			model.addAttribute("loginMeminfo", result);
			return showMessageWithRedirect("로그인이 완료되었습니다.", "../index", Method.GET, null, model);
			
		} catch (DataAccessException e) {
			return showMessageWithRedirect("데이터베이스 처리 과정에 문제가 발생하였습니다.", "/member/login", Method.GET, null, model);

		} catch (Exception e) {
			return showMessageWithRedirect("시스템에 문제가 발생하였습니다.", "/member/login", Method.GET, null, model);
		}

	}

	/** 이메일 인증 **/
	@PostMapping("/sendEmail") // 이메일 인증 코드 보내기
	@ResponseBody
	public void emailConfirm(@RequestParam String memEmail) throws Exception {
		System.out.println("전달 받은 이메일 : " + memEmail);
		memberService.sendSimpleMessage(memEmail);

	}

	@PostMapping("/verifyCode") // 이메일 인증 코드 검증
	@ResponseBody
	public int verifyCode(@RequestParam String code, Model model) {

		int result = 0;
		System.out.println("code : " + code);
		System.out.println("code match : " + MemberServiceImpl.ePw.equals(code));
		if (MemberServiceImpl.ePw.equals(code)) {
			result = 1;
		}

		return result;
	}

}

package com.game.controller;

import java.util.List;

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
import com.game.domain.CommentDTO;
import com.game.domain.GameScoreDTO;
import com.game.domain.MemberDTO;
import com.game.service.BoardService;
import com.game.service.CommentService;
import com.game.service.GameService;
import com.game.service.MemberService;

@Controller
@RequestMapping("/mypage/*")
public class MypageController extends UiUtils {

	@Autowired
	private MemberService memberService;

	@Autowired
	private BoardService boardService;

	@Autowired
	private CommentService commentService;

	@Autowired
	private GameService gameService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping(value = "/mypagemain")
	public String mypagemain(@SessionAttribute(name = "loginMem", required = false) MemberDTO loginMember,
			Model model) {
		/*
		 * 로그인 세션이 없을때 메인페이지 이동. 테스트중일땐 주석처리 if (loginMember == null) { return
		 * showMessageWithRedirect("로그인이 필요합니다", "/index", Method.GET, null, model); }
		 */
		return "mypage/mypagemain";
	}

	@GetMapping(value = "/userboardview")
	public String userboardview(@SessionAttribute(name = "loginMem", required = false) MemberDTO loginMember,
			@ModelAttribute("params") BoardDTO params, Model model) {
		/*
		 * 로그인 세션이 없을때 메인페이지 이동. 테스트중일땐 주석처리 if (loginMember == null) { return
		 * showMessageWithRedirect("로그인이 필요합니다", "/index", Method.GET, null, model); }
		 */
		// params.setMemId(loginMember.getMemId());
		params.setMemId("admin"); // 테스트용 하드코딩.
		params.setBoardType(1);
		List<BoardDTO> boardList = boardService.getBoardList(params);
		model.addAttribute("boardList", boardList);
		return "mypage/userboardview";
	}

	@GetMapping(value = "/usercommentview")
	public String usercommentview(@SessionAttribute(name = "loginMem", required = false) MemberDTO loginMember,
			@ModelAttribute("params") CommentDTO params, Model model) {
		/*
		 * 로그인 세션이 없을때 메인페이지 이동. 테스트중일땐 주석처리 if (loginMember == null) { return
		 * showMessageWithRedirect("로그인이 필요합니다", "/index", Method.GET, null, model); }
		 */
		params.setMemId("admin"); // 테스트용 하드코딩.
		List<CommentDTO> commList = commentService.selectMyComment(params);
		model.addAttribute("commList", commList);

		return "mypage/usercommentview";
	}

	@GetMapping(value = "/userrankview")
	public String userrankview(@SessionAttribute(name = "loginMem", required = false) MemberDTO loginMember,
			Model model) {
		/*
		 * 로그인 세션이 없을때 메인페이지 이동. 테스트중일땐 주석처리 if (loginMember == null) { return
		 * showMessageWithRedirect("로그인이 필요합니다", "/index", Method.GET, null, model); }
		 */
		return "mypage/userrankview";
	}

	@GetMapping(value = "/userupdate")
	public String userupdatefunction(@SessionAttribute(name = "loginMem", required = false) MemberDTO loginMember,
			Model model) {
		System.out.println("사용자정보 접속");
		MemberDTO user = new MemberDTO();
		user.setMemId(loginMember.getMemId());
		user = memberService.selectMember(user);
		model.addAttribute("user", user);
		return "mypage/userupdate";
	}

	/** 사용자 정보 수정 **/
	@PostMapping(value = "/userupdate")
	public String userupdatefunction(MemberDTO member, Model model,
			@SessionAttribute(name = "loginMem", required = false) MemberDTO loginMember) {
		// System.out.println("함수진입1");
		// System.out.println(member.getMemNick()); //memNick
		// System.out.println(member.getMemHp());
		// System.out.println(member.getMemEmail());
		member.setMemId(loginMember.getMemId());
		try {
			int userupdate = memberService.userUpdate(member);
			if (userupdate == 0) {
				System.out.println("수정되나요");
				return showMessageWithRedirect("사용자 정보 수정에 실패하였습니다.", "/mypage/userupdate", Method.GET, null, model);
			}

		} catch (DataAccessException e) {
			return showMessageWithRedirect("데이터베이스 처리 과정에 문제가 발생하였습니다.", "/mypage/userupdate", Method.GET, null, model);

		} catch (Exception e) {
			return showMessageWithRedirect("시스템에 문제가 발생하였습니다.", "/mypage/userupdate", Method.GET, null, model);
		}

		return showMessageWithRedirect("사용자 정보 수정이 완료되었습니다.", "/mypage/userupdate", Method.GET, null, model);
	}

	// 닉네임 중복검사
	@PostMapping(value = "/mypage/nickcheck.do")
	@ResponseBody
	public int JoinNickCheck(@RequestParam String memNick) {
		// System.out.println("nickcheck");
		// System.out.println(memNick);
		return memberService.memberNickcheck(memNick);
	}

	// 핸드폰번호 중복검사
	@PostMapping(value = "/mypage/hpcheck.do")
	@ResponseBody
	public int JoinHpCheck(@RequestParam String memHp) {
		// System.out.println("핸드폰번호체크~~~~~~");
		// System.out.println(memHp);
		return memberService.memberHpcheck(memHp);
	}

	// 이메일 중복검사
	@PostMapping(value = "/mypage/emailcheck.do")
	@ResponseBody
	public int JoinEmailCheck(@RequestParam String memEmail) {
		// System.out.println("이메일체크~~~~~~");
		// System.out.println(memEmail);
		return memberService.memberEmailcheck(memEmail);
	}

	// 회원탈퇴
	@GetMapping(value = "/userdelete")
	public String userDelete(Model model) {
		return "mypage/userdelete";
	}

	@PostMapping(value = "/userdelete")
	public String userDelete(@SessionAttribute(name = "loginMem", required = false) MemberDTO loginMember,
			MemberDTO member, Model model, HttpServletRequest request) {
		System.out.println(loginMember.getMemId());
		System.out.println(member.getMemId());
		try {

			if (loginMember.getMemId().equals(member.getMemId()) == false) {
				System.out.println("함수진입2");
				return showMessageWithRedirect("아이디가 틀렸습니다.", "/mypage/userdelete", Method.GET, null, model);
			}

			if (!passwordEncoder.matches(member.getMemPass(), loginMember.getMemPass())) {
				System.out.print("함수진입3");
				return showMessageWithRedirect("비밀번호가 일치하지 않습니다..", "/member/login", Method.GET, null, model);
			}

			int userdropsuccess = memberService.dropMember(loginMember);
			if (userdropsuccess == 0) {
				return showMessageWithRedirect("회원탈퇴 실패", "/mypage/userdelete", Method.GET, null, model);
			}
		} catch (DataAccessException e) {
			return showMessageWithRedirect("데이터베이스 처리 과정에 문제가 발생하였습니다.", "/mypage/userdelete", Method.GET, null, model);

		} catch (Exception e) {
			return showMessageWithRedirect("시스템에 문제가 발생하였습니다.", "/mypage/userdelete", Method.GET, null, model);
		}
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate(); // 세션 날림
		}
		return showMessageWithRedirect("회원탈퇴 성공", "../index", Method.GET, null, model);

	}

	// 내 게시글 삭제
	@GetMapping(value = "/boardDelete")
	public String boardDelete(@ModelAttribute("params") BoardDTO params,
			@SessionAttribute(name = "loginMem", required = false) MemberDTO loginMember, Model model) {
		/***************** 로그인 세션 구현시 *******************/
		// params.setBoardUpdateId(loginMember.getMemId());
		// params.setMemId(loginMember.getMemId());
		/************************************************/
		params.setBoardUpdateId("admin");// 테스트용 하드코딩
		params.setMemId("admin");// 테스트용 하드코딩
		try {
			if (boardService.deleteBoard(params)) {
				return showMessageWithRedirect("삭제가 완료되었습니다.", "/mypage/userboardview", Method.GET, null, model);
			} else {
				return showMessageWithRedirect("삭제를 실패하였습니다.", "/mypage/userboardview", Method.GET, null, model);
			}
		} catch (DataAccessException e) {
			return showMessageWithRedirect("데이터베이스 처리 과정에 문제가 발생하였습니다.", "/mypage/userboardview", Method.GET, null,
					model);

		} catch (Exception e) {
			return showMessageWithRedirect("시스템에 문제가 발생하였습니다.", "/mypage/userboardview", Method.GET, null, model);
		}
	}

	// 내 댓글 삭제
	@GetMapping(value = "/commentDelete")
	public String commentdelete(@ModelAttribute("params") CommentDTO params,
			@SessionAttribute(name = "loginMem", required = false) MemberDTO loginMember, Model model) {
		/***************** 로그인 세션 구현시 *******************/
		// params.setCommUpdateId(loginMember.getMemId());
		// params.setMemId(loginMember.getMemId());
		/************************************************/
		params.setCommUpdateId("admin");// 테스트용 하드코딩->글삭제 시도 회원 (댓글작성자 또는 관리자)
		params.setMemId("admin");// 테스트용 하드코딩->댓글 작성자
		try {
			if (commentService.deleteComment(params)) {
				return showMessageWithRedirect("삭제가 완료되었습니다.", "/mypage/usercommentview", Method.GET, null, model);
			} else {
				return showMessageWithRedirect("삭제를 실패하였습니다.", "/mypage/usercommentview", Method.GET, null, model);
			}
		} catch (DataAccessException e) {
			return showMessageWithRedirect("데이터베이스 처리 과정에 문제가 발생하였습니다.", "/mypage/usercommentview", Method.GET, null,
					model);

		} catch (Exception e) {
			return showMessageWithRedirect("시스템에 문제가 발생하였습니다.", "/mypage/usercommentview", Method.GET, null, model);
		}
	}

	@GetMapping(value = "/menulist")
	public String menulist(Model model) {
		return "mypage/menulist";
	}

	@GetMapping(value = "/dinorank")
	public String dinorank(@SessionAttribute(name = "loginMem", required = false) MemberDTO loginMember, Model model) {
		GameScoreDTO user = new GameScoreDTO();
		// user.setMemId(loginMember.getMemId());
		user.setMemId("uuu333");
		user.setGameName("공룡게임");

		List<GameScoreDTO> rank = gameService.selectMyRank(user);
		model.addAttribute("dino", rank);
		return "mypage/dinorank";
	}

	@GetMapping(value = "/ddongrank")
	public String ddongrank(@SessionAttribute(name = "loginMem", required = false) MemberDTO loginMember, Model model) {
		GameScoreDTO user = new GameScoreDTO();
		// user.setMemId(loginMember.getMemId());
		user.setMemId("uuu333");
		user.setGameName("똥피하기");

		List<GameScoreDTO> rank = gameService.selectMyRank(user);
		model.addAttribute("ddong", rank);
		return "mypage/ddongrank";
	}

}

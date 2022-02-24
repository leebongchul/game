package com.game.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.game.Util.UiUtils;
import com.game.constant.Method;
import com.game.domain.BoardDTO;
import com.game.domain.CommentDTO;
import com.game.domain.MemberDTO;
import com.game.domain.ReportDTO;
import com.game.service.BoardService;
import com.game.service.CommentService;
import com.game.service.MemberService;

@Controller
@RequestMapping("/admin/*")
public class AdminController extends UiUtils {

	@Autowired
	private BoardService boardService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private CommentService commentService;

	@GetMapping(value = "/test")
	public String testfunction(Model model) {
		return "admin/test";
	}

	@GetMapping(value = "/adminpagemain")
	public String adminpageMain(Model model) {
		return "admin/adminpagemain";
	}

	@GetMapping(value = "/mainboard")
	public String openMainboardpage(@ModelAttribute("params") BoardDTO params, Model model) {
		if (params.getBoardType() == 0) {
			params.setBoardType(1);
		}
		List<BoardDTO> boardList = boardService.getBoardList(params);
		model.addAttribute("boardList", boardList);
		return "admin/mainboard";
	}

	// 게시글 삭제
	@GetMapping(value = "/boardDelete")
	public String boardDelete(@ModelAttribute("params") BoardDTO params,
			@SessionAttribute(name = "loginMem", required = false) MemberDTO loginMember, Model model) {
		/***************** 로그인 세션 구현시 *******************/
//		params.setBoardUpdateId(loginMember.getMemId());
//		params.setMemId(loginMember.getMemId());
//		params.setMemRole(loginMember.getMemRole());
		/************************************************/
		params.setBoardUpdateId("admin");// 테스트용 하드코딩
		params.setMemId("admin");// 테스트용 하드코딩
		params.setMemRole("user");// 테스트용 하드코딩
		try {
			if (boardService.deleteBoard(params)) {
				return showMessageWithRedirect("삭제가 완료되었습니다.", "/admin/mainboard", Method.GET, null, model);
			} else {
				if (params.getMemRole().equals("user")) {
					return showMessageWithRedirect("게시글 삭제 권한이 없습니다.", "/admin/mainboard", Method.GET, null, model);
				}
				return showMessageWithRedirect("삭제를 실패하였습니다.", "/admin/mainboard", Method.GET, null, model);
			}
		} catch (

		DataAccessException e) {
			return showMessageWithRedirect("데이터베이스 처리 과정에 문제가 발생하였습니다.", "/admin/mainboard", Method.GET, null, model);

		} catch (Exception e) {
			return showMessageWithRedirect("시스템에 문제가 발생하였습니다.", "/admin/mainboard", Method.GET, null, model);
		}
	}

	// 댓글 삭제
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

	@GetMapping(value = "/report/list")
	public String openReportpage(Model model) {
		List<ReportDTO> reportList = boardService.getReportList();
		if (reportList == null) {
			// 신고가 없을 때
			return "member/test";
		}
		model.addAttribute("reportList", reportList);

		return "admin/report";
	}

	@GetMapping(value = "/block")
	public String blockUser(@ModelAttribute("params") MemberDTO params, Model model) {
		params.setMemBlock("Y");
		try {
			if (params.getMemId() != null && params.getBlockPeriod() != 0) {
				if (memberService.updateMemberBlock(params)) {
					return showMessageWithRedirect("차단이 완료되었습니다.", "/admin/report/list", Method.GET, null, model);
				}
				return showMessageWithRedirect("차단을 실패하였습니다.", "/admin/report/list", Method.GET, null, model);
			}
		} catch (DataAccessException e) {
			return showMessageWithRedirect("데이터베이스 처리 과정에 문제가 발생하였습니다.", "/admin/report/list", Method.GET, null, model);

		} catch (Exception e) {
			return showMessageWithRedirect("시스템에 문제가 발생하였습니다.", "/admin/report/list", Method.GET, null, model);
		}
		return "admin/report";
	}

}
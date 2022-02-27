package com.game.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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
	public String adminpageMain(@SessionAttribute(name = "loginMem", required = false) MemberDTO loginMember,
			Model model) {

		model.addAttribute("member", loginMember);
		return "admin/adminpagemain";
	}

	@GetMapping(value = "/report")
	public String openReportpage(@SessionAttribute(name = "loginMem", required = false) MemberDTO loginMember,
			@ModelAttribute("params") ReportDTO params, Model model) {
		List<ReportDTO> reportList = boardService.getReportList(params);
		if (reportList == null) {
			// 신고가 없을 때
			return "member/test";
		}

		model.addAttribute("member", loginMember);
		model.addAttribute("reportList", reportList);

		return "admin/report";
	}

	@GetMapping(value = "/comment")
	public String openCommentpage(@SessionAttribute(name = "loginMem", required = false) MemberDTO loginMember,
			@ModelAttribute("params") CommentDTO params, Model model) {
		if (loginMember == null) {
			return showMessageWithRedirect("로그인이 필요합니다", "/index", Method.GET, null, model);
		}

		/******************************
		 * 권한 설정-> 테스트 완료시 주석 해제
		 ************************/
//      if (loginMember.getMemRole().equals("user")) {
//          return showMessageWithRedirect("페이지 접속 권한이 없습니다. 관리자 계정으로 로그인하세요.", "/index", Method.GET, null, model);
//      }
		/*****************************************************************/
		params.setMemId(loginMember.getMemId());
		List<CommentDTO> commList = commentService.getCommentList(params);
		model.addAttribute("commList", commList);
		model.addAttribute("member", loginMember);

		return "admin/comment";
	}

	@GetMapping(value = "/noticelist")
	public String openNoticeBoardList(@ModelAttribute("params") BoardDTO params,
			@SessionAttribute(name = "loginMem", required = false) MemberDTO loginMember, Model model) {
		// 메인 생성되면 보드타입 변경?
		params.setBoardType(2);
		List<BoardDTO> boardList = boardService.getBoardList(params);
		model.addAttribute("boardList", boardList);

		// 비회원
		if (loginMember == null) {
			System.out.println("게스트접속");
			model.addAttribute("member", new MemberDTO());
		} else {
			model.addAttribute("member", loginMember);
		}

		return "admin/noticelist";
	}

	// 공지사항 글쓰기, 상세보기-수정화면
	@GetMapping(value = "/noticeboard/write")
	public String openNoticeBoardWrite(@ModelAttribute("params") BoardDTO params,
			@SessionAttribute(name = "loginMem", required = false) MemberDTO loginMember, Model model) {
		if (loginMember == null || loginMember.getMemRole().equals("user")) {
			return showMessageWithRedirect("글쓰기 권한이 없습니다. 관리자 계정으로 로그인해주세요.", "/noticeboard/list", Method.GET, null,
					model);
		}
		if (params.getBoardNum() == null) { // 글쓰기 화면
			/*********** 로그인 세션 구현시 ***************/
			params.setMemId(loginMember.getMemId());
			params.setMemNick(loginMember.getMemNick());
			/******************************************/

			model.addAttribute("board", params);
		} else {
			BoardDTO board = boardService.getBoardDetail(params);
			if (board == null) {
				return "redirect:/noticeboard/list";
			}
			model.addAttribute("board", board);
		}

		return "admin/noticewrite";
	}

	// 글쓰기,상세보기-수정화면에서 '저장하기 클릭 시'
	@PostMapping(value = "/noticeboard/register")
	public String registerNoticeBoard(@ModelAttribute("params") BoardDTO params, Model model) {
		// 현재 로그인 계정(boardUpdateId)이랑 게시글 상세보기에서 넘어온 글작성자(memId)와 비교해서 갈을 때 BoardDTO 넘김
		// 22.02.16 현재 로그인 계정 구현 안되어있어, boardUpdateId를 글작성자로 해서 넘김
		System.out.println("공지사항 등록");
		params.setBoardUpdateId(params.getMemId());
		try {
			// 로그인 계정과 params.get
			boolean isRegistered = boardService.registerBoard(params);
			if (isRegistered == false) {
				return showMessageWithRedirect("게시글 등록에 실패하였습니다.", "/board/noticeboard/list", Method.GET, null, model);
			}
		} catch (DataAccessException e) {
			return showMessageWithRedirect("데이터베이스 처리 과정에 문제가 발생하였습니다.", "/board/noticeboard/list", Method.GET, null,
					model);

		} catch (Exception e) {
			return showMessageWithRedirect("시스템에 문제가 발생하였습니다.", "/board/noticeboard/list", Method.GET, null, model);
		}

		return showMessageWithRedirect("게시글 등록이 완료되었습니다.", "/board/noticeboard/list", Method.GET, null, model);
	}

	// 공시사항 삭제
	@GetMapping(value = "/boardDelete")
	public String boardDelete(@ModelAttribute("params") BoardDTO params,
			@SessionAttribute(name = "loginMem", required = false) MemberDTO loginMember, Model model) {
		/***************** 로그인 세션 구현시 *******************/
		params.setBoardUpdateId(loginMember.getMemId());
		params.setMemId(loginMember.getMemId());
		params.setMemRole(loginMember.getMemRole());
		/************************************************/
//		params.setBoardUpdateId("admin");// 테스트용 하드코딩
//		params.setMemId("admin");// 테스트용 하드코딩
//		params.setMemRole("user");// 테스트용 하드코딩
		try {
			if (boardService.deleteBoard(params)) {
				return showMessageWithRedirect("삭제가 완료되었습니다.", "/board/noticeboard/list", Method.GET, null, model);
			} else {
				if (params.getMemRole().equals("user")) {
					return showMessageWithRedirect("게시글 삭제 권한이 없습니다.", "/board/noticeboard/list", Method.GET, null,
							model);
				}
				return showMessageWithRedirect("삭제를 실패하였습니다.", "/board/noticeboard/list", Method.GET, null, model);
			}
		} catch (

		DataAccessException e) {
			return showMessageWithRedirect("데이터베이스 처리 과정에 문제가 발생하였습니다.", "/board/noticeboard/list", Method.GET, null,
					model);

		} catch (Exception e) {
			return showMessageWithRedirect("시스템에 문제가 발생하였습니다.", "/board/noticeboard/list", Method.GET, null, model);
		}
	}

	// 댓글 삭제
	@GetMapping(value = "/commentDelete")
	public String commentdelete(@ModelAttribute("params") CommentDTO params,
			@SessionAttribute(name = "loginMem", required = false) MemberDTO loginMember, Model model) {
		/***************** 로그인 세션 구현시 *******************/
		params.setCommUpdateId(loginMember.getMemId());
//		params.setMemId(loginMember.getMemId());
		/************************************************/
//		params.setCommUpdateId("admin");// 테스트용 하드코딩->글삭제 시도 회원 (댓글작성자 또는 관리자)
//		params.setMemId("admin");// 테스트용 하드코딩->댓글 작성자
		try {
			if (commentService.deleteComment(params)) {
				return showMessageWithRedirect("삭제가 완료되었습니다.", "/admin/comment", Method.GET, null, model);
			} else {
				return showMessageWithRedirect("삭제를 실패하였습니다.", "/admin/comment", Method.GET, null, model);
			}
		} catch (DataAccessException e) {
			return showMessageWithRedirect("데이터베이스 처리 과정에 문제가 발생하였습니다.", "/admin/comment", Method.GET, null, model);

		} catch (Exception e) {
			return showMessageWithRedirect("시스템에 문제가 발생하였습니다.", "/admin/comment", Method.GET, null, model);
		}
	}

	@GetMapping(value = "/block")
	public String blockUser(@ModelAttribute("params") MemberDTO params, Model model) {
		params.setMemBlock("Y");
		try {
			if (params.getMemId() != null && params.getBlockPeriod() != 0) {
				if (memberService.updateMemberBlock(params)) {
					return showMessageWithRedirect("차단이 완료되었습니다.", "/admin/report", Method.GET, null, model);
				}
				return showMessageWithRedirect("차단을 실패하였습니다.", "/admin/report", Method.GET, null, model);
			}
		} catch (DataAccessException e) {
			return showMessageWithRedirect("데이터베이스 처리 과정에 문제가 발생하였습니다.", "/admin/report", Method.GET, null, model);

		} catch (Exception e) {
			return showMessageWithRedirect("시스템에 문제가 발생하였습니다.", "/admin/report", Method.GET, null, model);
		}
		return "admin/report";
	}

}
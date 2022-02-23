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
import com.game.domain.GameScoreDTO;
import com.game.domain.MemberDTO;
import com.game.domain.ReportDTO;
import com.game.service.BoardService;
import com.game.service.GameService;

@Controller
@RequestMapping("/board/*")
public class BoardController extends UiUtils {

	@Autowired
	private BoardService boardService;

	@Autowired
	private GameService gameService;

	@GetMapping(value = "/test")
	public String testfunction(Model model) {
		return "board/test";
	}

	@GetMapping(value = "/freeboard/list")
	public String openBoardList(@ModelAttribute("params") BoardDTO params, Model model) {
		// 메인 생성되면 보드타입 변경?
		params.setBoardType(1);
		List<BoardDTO> boardList = boardService.getBoardList(params);
		model.addAttribute("boardList", boardList);

		return "board/list";
	}

	@GetMapping(value = "/rank")
    public String openRankList() {
        return "board/rank";
    }
    
    @GetMapping(value = "/layout/dinorank")
    public String opendinoList(@ModelAttribute("params") GameScoreDTO params, Model model) {
        params.setGameName("공룡게임");
        List<GameScoreDTO> dinorank = gameService.selectGameRankList(params);
        model.addAttribute("dino", dinorank);
        
        return "board/layout/dinorank";
    }
    
    @GetMapping(value = "/layout/ddongrank")
    public String openddongList(@ModelAttribute("params") GameScoreDTO params, Model model) {
        params.setGameName("똥피하기");
        List<GameScoreDTO> ddongrank = gameService.selectGameRankList(params);
        model.addAttribute("ddong", ddongrank);
        
        return "board/layout/ddongrank";
    }
///////////////////////////////////////////////////////////공지사항 테스트중 Start

	@GetMapping(value = "/noticeboard/list")
	public String openNoticeBoardList(@ModelAttribute("params") BoardDTO params, Model model) {
		// 메인 생성되면 보드타입 변경?
		params.setBoardType(2);
		List<BoardDTO> boardList = boardService.getBoardList(params);
		model.addAttribute("boardList", boardList);

		return "admin/list2";
	}

	@GetMapping(value = "/noticeboard/view")
	public String openNoticeBoardDetail(@ModelAttribute("params") BoardDTO params, Model model) {
		System.out.println("boardNum:" + params.getBoardNum());
		if (params.getBoardNum() == null) {
			return showMessageWithRedirect("올바르지 않은 접근입니다. 목록화면으로 이동합니다,", "/board/noticeboard/list", Method.GET, null,
					model);
		}
		BoardDTO board = boardService.getBoardDetail(params);
		if (board == null || "Y".equals(board.getBoardDelete())) {
			return showMessageWithRedirect("없거나 이미 삭제된 게시글입니다. 목록화면으로 이동합니다,", "/board/noticeboard/list", Method.GET,
					null, model);
		}
		model.addAttribute("board", board);

		return "admin/view2";
	}

///////////////////////////////////////////////////////////공지사항 테스트중 End

///////////////////////////////////////////////////////////신고하기 테스트중 Start

	@GetMapping(value = "/report")
	public String reportBoard(@ModelAttribute("params") ReportDTO params,
			@SessionAttribute(name = "loginMem", required = false) MemberDTO loginMember, Model model) {
		/*********** 로그인 세션 구현시 ***************/
//		params.setMemId(loginMember.getMemId());
		/******************************************/
		params.setMemId("admin");
		String url = "/board/noticeboard/view?boardNum=" + params.getBoardNum() + "&&boardType=" + params.getBoardType()
				+ "&&memId=" + params.getRepId();
		try {
			if (boardService.registerReport(params)) {
				return showMessageWithRedirect("신고가 완료되었습니다.", url, Method.GET, null, model);
			}
		} catch (DataAccessException e) {
			return showMessageWithRedirect("데이터베이스 처리 과정에 문제가 발생하였습니다.", url, Method.GET, null, model);

		} catch (Exception e) {
			return showMessageWithRedirect("시스템에 문제가 발생하였습니다.", url, Method.GET, null, model);
		}

//		model.addAttribute("boardList", boardList);

		return "admin/list2";
	}
///////////////////////////////////////////////////////////신고하기 테스트중 End

	@GetMapping(value = "/freeboard/view")
	public String openBoardDetail(@ModelAttribute("params") BoardDTO params, Model model) {
		System.out.println("boardNum:" + params.getBoardNum());
		if (params.getBoardNum() == null) {
			return showMessageWithRedirect("올바르지 않은 접근입니다. 목록화면으로 이동합니다.", "/board/freeboard/list", Method.GET, null,
					model);
		}
		BoardDTO board = boardService.getBoardDetail(params);
		if (board.getBoardNum() == null || "Y".equals(board.getBoardDelete())) {
			return showMessageWithRedirect("없거나 이미 삭제된 게시글입니다. 목록화면으로 이동합니다.", "/board/freeboard/list", Method.GET,
					null, model);
		}
		boardService.plusBoardHit(params);
		model.addAttribute("board", board);

		return "board/view";
	}

	@GetMapping(value = "/freeboard/write") // 글쓰기, 상세보기-수정화면
	public String openBoardWrite(@ModelAttribute("params") BoardDTO params,
			@SessionAttribute(name = "loginMem", required = false) MemberDTO loginMember, Model model) {
		if (params.getBoardNum() == null) { // 글쓰기 화면
			/*********** 로그인 세션 구현시 ***************/
//			params.setMemId(loginMember.getMemId());
//			params.setMemNick(loginMember.getMemNick());
			/******************************************/
			params.setMemId("khb");
			params.setMemNick("테스트2");

			model.addAttribute("board", params);
		} else {
			BoardDTO board = boardService.getBoardDetail(params);
			if (board == null) {
				return "redirect:/board/freeboard/list";
			}
			model.addAttribute("board", board);
		}

		return "board/write";
//		model.addAttribute("board", params);
//
//		return "board/view";
	}

	@PostMapping(value = "/freeboard/register") // 글쓰기,상세보기-수정화면에서 '저장하기 클릭 시'
	public String registerBoard(@ModelAttribute("params") BoardDTO params, Model model) {
		// 현재 로그인 계정(boardUpdateId)이랑 게시글 상세보기에서 넘어온 글작성자(memId)와 비교해서 갈을 때 BoardDTO 넘김
		// 22.02.16 현재 로그인 계정 구현 안되어있어, boardUpdateId를 글작성자로 해서 넘김
		params.setBoardUpdateId(params.getMemId());
		try {
			// 로그인 계정과 params.get
			boolean isRegistered = boardService.registerBoard(params);
			if (isRegistered == false) {
				return showMessageWithRedirect("게시글 등록에 실패하였습니다.", "/board/freeboard/list", Method.GET, null, model);
			}
		} catch (DataAccessException e) {
			return showMessageWithRedirect("데이터베이스 처리 과정에 문제가 발생하였습니다.", "/board/freeboard/list", Method.GET, null,
					model);

		} catch (Exception e) {
			return showMessageWithRedirect("시스템에 문제가 발생하였습니다.", "/board/freeboard/list", Method.GET, null, model);
		}

		return showMessageWithRedirect("게시글 등록이 완료되었습니다.", "/board/freeboard/list", Method.GET, null, model);
	}

	@PostMapping(value = "/freeboard/delete")
	public String deleteBoard(@ModelAttribute("params") BoardDTO params, Model model) {
		if (params.getBoardNum() == null && params.getMemId() == null) {
			return showMessageWithRedirect("올바르지 않은 접근입니다.", "/board/freeboard/list", Method.GET, null, model);
		}
		// 현재 로그인 계정을 boardUpdateId로 넘겨줘서 삭제 시 해당 아이디 등록//로그인 구현x상태로 작성자 아이디 입력하여 넘겨준 상태
		params.setBoardUpdateId("admin");
		try {
			boolean isDeleted = boardService.deleteBoard(params);
			if (isDeleted == false) {
				return showMessageWithRedirect("게시글 삭제에 실패하였습니다.", "/board/freeboard/list", Method.GET, null, model);
			}
		} catch (DataAccessException e) {
			return showMessageWithRedirect("데이터베이스 처리 과정에 문제가 발생하였습니다.", "/board/freeboard/list", Method.GET, null,
					model);

		} catch (Exception e) {
			return showMessageWithRedirect("시스템에 문제가 발생하였습니다.", "/board/freeboard/list", Method.GET, null, model);
		}

		return showMessageWithRedirect("게시글 삭제가 완료되었습니다.", "/board/freeboard/list", Method.GET, null, model);
	}

	@GetMapping(value = "/freeboard/delete")
	public String GetdeleteBoard(@ModelAttribute("params") BoardDTO params, Model model) {
		if (params.getBoardNum() == null && params.getBoardType() == 0 && params.getMemId() == null) {
			return showMessageWithRedirect("올바르지 않은 접근입니다.", "/board/freeboard/list", Method.GET, null, model);
		}
		// 현재 로그인 계정을 boardUpdateId로 넘겨줘서 삭제 시 해당 아이디 등록//로그인 구현x상태로 작성자 아이디 입력하여 넘겨준 상태
		params.setBoardUpdateId(params.getMemId());
		try {
			boolean isDeleted = boardService.deleteBoard(params);
			if (isDeleted == false) {
				return showMessageWithRedirect("게시글 삭제에 실패하였습니다.", "/board/freeboard/list", Method.GET, null, model);
			}
		} catch (DataAccessException e) {
			return showMessageWithRedirect("데이터베이스 처리 과정에 문제가 발생하였습니다.", "/board/freeboard/list", Method.GET, null,
					model);

		} catch (Exception e) {
			return showMessageWithRedirect("시스템에 문제가 발생하였습니다.", "/board/freeboard/list", Method.GET, null, model);
		}

		return showMessageWithRedirect("게시글 삭제가 완료되었습니다.", "/board/freeboard/list", Method.GET, null, model);
	}
	
	
//	@GetMapping(value = "/freeboard/searchlist")
//	public String openBoardList(@ModelAttribute("params") BoardDTO params, Model model) {
//		// 메인 생성되면 보드타입 변경?
//		params.setBoardType(1);
//		List<BoardDTO> boardList = boardService.getBoardList(params);
//		model.addAttribute("boardList", boardList);
//
//		return "board/list";
//	}

	

}

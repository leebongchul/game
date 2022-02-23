package com.game.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.game.Util.UiUtils;
import com.game.constant.Method;
import com.game.domain.MemberDTO;
import com.game.domain.ReportDTO;
import com.game.service.BoardService;
import com.game.service.MemberService;

@Controller
@RequestMapping("/admin/*")
public class AdminController extends UiUtils {

	@Autowired
	private BoardService boardService;
	@Autowired
	private MemberService memberService;

	@GetMapping(value = "/test")
	public String testfunction(Model model) {
		return "admin/test";
	}
	
	@GetMapping(value = "/adminpagemain")
    public String adminpageMain(Model model) {
        return "admin/adminpagemain";
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
		params.setMemBlock("N");
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

//	@GetMapping(value = "/block")
//	public String blockUser(@ModelAttribute("params") MemberDTO params, Model model) {
//		if (params != null) {
//			model.addAttribute("params", params);
//			return "admin/test";
//		}
//
//		return "admin/report";
//	}

}
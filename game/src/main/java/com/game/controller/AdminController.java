package com.game.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.game.Util.UiUtils;
import com.game.domain.BoardDTO;
import com.game.domain.ReportDTO;
import com.game.service.BoardService;

@Controller
@RequestMapping("/admin/*")
public class AdminController extends UiUtils {

	@Autowired
	private BoardService boardService;

	@GetMapping(value = "/test")
	public String testfunction(Model model) {
		return "board/test";
	}

//	@GetMapping(value = "/report")
//	public String openReportpage(Model model) {
//		return "admin/report";
//	}

	@GetMapping(value = "/report/list")
	public String openReportpage(@ModelAttribute("params") BoardDTO params, Model model) {
		List<ReportDTO> reportList = boardService.getReportList();
		model.addAttribute("reportList", reportList);

		return "admin/report";
	}

}
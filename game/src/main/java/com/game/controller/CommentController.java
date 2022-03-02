package com.game.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.game.adapter.GsonLocalDateTimeAdapter;
import com.game.domain.CommentDTO;
import com.game.domain.MemberDTO;
import com.game.service.CommentService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@RestController
public class CommentController {

	@Autowired
	private CommentService commentService;

	// rest api의 형식을 따르기 위해, 댓글 생성인경우 , 댓글 수정인경우로 매핑 됨.
	// 댓글이 처음 생성되면 POST방식으로 매핑을 받고, 댓글이 이미 있는걸 수정할때는 PATCH방식으로 매핑을 받음

	// @RequestBody는 파라미터로 전달받은 JSON 문자열을 객체로 변환.
	// <실행과정>
	// 1. 클라이언트(사용자)는 게시글 번호, 댓글 내용, 댓글 작성자를 JSON 문자열로 전송한다.
	// 2. 서버(컨트롤러)는 JSON 문자열을 파라미터로 전달받는다.
	// 3. @RequestBody는 전달받은 JSON 문자열을 객체로 변환한다.
	// 4. 객체로 변환된 JSON은 CommentDTO 클래스의 객체인 params에 매핑(바인딩)된다.

	// <메서드 역할>
	// @RequestMapping이 아래같은 작업을 가능케 함
	// 1. POST: 자원생성
	// 2. GET: 자원조회
	// 3. PUT: 자원수정
	// 4. PATCH: 자원수정
	// 5. DELET: 자원삭제

	// 댓글이 없을땐 자원을 생성해야 하기때문에 무조건 POST로 매핑해야하는데 왜 매핑이 안돼냐..
	// @PathVariable = @RequestParams와 유사한 기능. URI에 파라미터로 전달받을 변수를 지정
	// ../{value}의 형태로 받음. @RequestParams은 /..?key1=value1&key2=value2...이런식
	@RequestMapping(value = { "/comments", "/comments/{commNum}" }, method = { RequestMethod.POST,
			RequestMethod.PATCH })
	public JsonObject registerComment(@PathVariable(value = "commNum", required = false) String str,
			@RequestBody final CommentDTO params) {
		JsonObject jsonObj = new JsonObject();

		try {
			// 댓글 pk가 null이 아니면 CommentDTO에 받아온 str 값을 저장.
			// str의 값이 없다면(=pk가 없따면) 변수 commNum에
			// null값이 넘어가서 서비스단의 registerComment() 에서 댓글 입력으로 연결된다.

			if (str != null) {
				params.setCommNum(str);
			}

			boolean isRegistered = commentService.registerComment(params);
			// 댓글 생성(or 수정)의 진행 결과를 "result"라는 프로퍼티로 등록

			jsonObj.addProperty("result", isRegistered);

		} catch (DataAccessException e) {
			jsonObj.addProperty("message", "데이터베이스 처리 과정에 문제가 발생하였습니다.");

		} catch (Exception e) {
			jsonObj.addProperty("message", "시스템에 문제가 발생하였습니다.");
		}

		return jsonObj;
	}

	@GetMapping(value = "/comments/{boardNum}")
	public JsonObject getCommentList(@PathVariable("boardNum") String str,
			@ModelAttribute("params") CommentDTO params, Model model) {
	    //22-03-01 파라미터에 모델 츠가
		System.out.println("params = " + params);
		/**
		 * rest 방식으로 입력된 데이터(=여기선 JSON)를 확인하기 쉽게 하기 위한 rest 클라이언트 프로그램 설치하면 좋긴 할듯
		 * https://install.advancedrestclient.com/install 나는 저 위 사이트에서 다운 받았음. 블로그에서
		 * 다운받으라는 거 해밨는데 안댐.
		 */

		// JSON 객체 생성
		JsonObject jsonObj = new JsonObject();

		// commentService.getCommentList(params) =
		// 특정 게시글에 포함된 댓글의 개수가 1이상이면, 특정 게시글의 모든 댓글 목록을 조회하는 메소드
		List<CommentDTO> commentList = commentService.getCommentList(params);

		// commentList의 리턴값이 false라면(=commentList가 null이 아니라면=댓글이 1개 이상 달렸다면)
		if (CollectionUtils.isEmpty(commentList) == false) {

			// 댓글의 날짜 표시 처리를 위한 코드
			Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter())
					.create();

			// Gson클래스의 메소드를 이용해서 commentList에 담긴 댓글들을 JsonArray타입으로 변환
			JsonArray jsonArr = gson.toJsonTree(commentList).getAsJsonArray();

			// JsonArray타입으로 변환된 객체를 다시 JSON객체에 삽입.
			// why? => JSON객체에 담아서 리턴하면 JSON객체가 됨으로써 다양한 데이터 형식을 가질 수 있기 때문.
			// * 한국영화정보진흥원? 작년에 자바스크립트 배울때 거기서 가져와서 실습했던 정보들이 JSON타입이었음.
			jsonObj.add("commentList", jsonArr);
			model.addAttribute("modelcomlist", jsonObj);


			
		}
		return jsonObj;

	}

//    @RequestMapping(value = "/comments/{commNum}", method = RequestMethod.DELETE)
	@DeleteMapping(value = "/comments/{commNum}")
	public JsonObject deleteComment(@PathVariable("commNum") final String str,
			@SessionAttribute(name = "loginMem", required = false) MemberDTO loginMember) {

		CommentDTO dto = commentService.selectCommentDetail(str);

		// 아이디값을 안주니까 삭제가 안됐음.
//		dto.setCommNum(str);
		dto.setCommUpdateId(loginMember.getMemId());

		JsonObject jsonObj = new JsonObject();

		try {
			boolean isDeleted = commentService.deleteComment(dto);
			jsonObj.addProperty("result", isDeleted);

		} catch (DataAccessException e) {
			jsonObj.addProperty("message", "데이터베이스 처리 과정에 문제가 발생하였습니다.");

		} catch (Exception e) {
			jsonObj.addProperty("message", "시스템에 문제가 발생하였습니다.");
		}

		return jsonObj;
	}

}
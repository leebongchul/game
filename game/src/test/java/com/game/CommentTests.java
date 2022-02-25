package com.game;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import com.game.domain.CommentDTO;
import com.game.mapper.CommentMapper;
import com.game.service.CommentService;

@SpringBootTest
public class CommentTests {

	@Autowired
	private CommentMapper commentMapper;

	@Autowired
	private CommentService commentService;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Test
	public void TestofinsertComment() {
		CommentDTO params = new CommentDTO();
		params.setBoardNum("b_68");
		params.setMemId("khb");
		params.setCommContents("댓글 테스트테스트2");

		System.out.println("테스트인풋 데이터 : " + params);
		int result = commentMapper.insertComment(params);
		System.out.println("테스트인풋 result : " + result);

	};

	@Test
	public void TestofselectCommentDetail() {
//        CommentDTO params = new CommentDTO();
//        params.setCommNum("c_53");

//		System.out.println("테스트인풋 데이터 : " + params);
		CommentDTO result = commentMapper.selectCommentDetail("c_53");
		System.out.println("테스트인풋 result : " + result);

	};

	@Test
	public void TestofupdateComment() {
		CommentDTO params = new CommentDTO();
		params.setCommContents("수정 테스트22");
		params.setCommNum("c_6");
		params.setMemId("khb0");

		System.out.println("테스트인풋 데이터 : " + params);
		int result = commentMapper.updateComment(params);
		System.out.println("테스트인풋 result : " + result);

	};

	@Test
	public void TestofdeleteComment() {
		CommentDTO params = new CommentDTO();
		params.setCommNum("c_30");
		params.setMemId("khb0");

		System.out.println("테스트인풋 데이터 : " + params);
		int result = commentMapper.deleteComment(params);
		System.out.println("테스트인풋 result : " + result);

	};

	@Test
	public void TestofselectCommentList() {
		CommentDTO params = new CommentDTO();
		params.setBoardNum("b_2");

		System.out.println("테스트인풋 데이터 : " + params);
		List<CommentDTO> result = commentMapper.selectCommentList(params);

		if (CollectionUtils.isEmpty(result) == false) {
			for (CommentDTO com : result) {
				System.out.println("=========================");
				System.out.println(com.getCommNum());
				System.out.println(com.getCommContents());
				System.out.println(com.getMemNick());
				System.out.println(com.getCommDate());
				System.out.println("=========================");
			}
		}
		System.out.println("테스트인풋 result : " + result);

	};

	@Test
	public void TestofselectCommentTotalCount() {
		CommentDTO params = new CommentDTO();
		params.setBoardNum("b_2");

		System.out.println("테스트인풋 데이터 : " + params);
		int result = commentMapper.selectCommentTotalCount(params);
		System.out.println("테스트인풋 result : " + result);

	};

	//////////////////////////////// CommentServiceImpl 코드들//////////////////////

	@Test
	public void registerComments() {
		int number = 20;

		for (int i = 1; i <= number; i++) {
			CommentDTO params = new CommentDTO();
			params.setBoardNum("b_5");
			params.setCommContents(i + "번 댓글을 추가합니다!");
			params.setMemId("khb0");
			commentService.registerComment(params);
		}

		logger.debug("댓글 " + number + "개가 등록되었습니다.");
	}

	@Test
	public void getCommentList() {
		CommentDTO params = new CommentDTO();
		params.setBoardNum("b_5"); // 댓글을 추가할 게시글 번호

		commentService.getCommentList(params);
	}

	@Test
	public void deleteComment() {
		CommentDTO params = new CommentDTO();
//      commentService.deleteComment(params.setCommNum("c_2")); 
		// 삭제할 댓글 번호
		params.setCommNum("c_32");
		params.setMemId("khb0");

		commentService.deleteComment(params);
		getCommentList();
		// logger.debug("댓글 " + result + "가 삭제되었습니다.");
	}

}

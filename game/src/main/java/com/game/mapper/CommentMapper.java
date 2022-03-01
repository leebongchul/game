package com.game.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.game.domain.BoardDTO;
import com.game.domain.CommentDTO;
import com.game.domain.ReportDTO;

@Mapper
public interface CommentMapper {

	/** 댓글 등록 */
	public int insertComment(CommentDTO parmas);

	/** 특정 댓글의 상세보기. 어차피 댓글은 그냥 보이니까 의미 없음. 댓글 삭제에 필요함. */
	public CommentDTO selectCommentDetail(String params);

	/** 댓글 수정 */
	public int updateComment(CommentDTO params);

	/** 댓글 삭제 */
	public int deleteComment(CommentDTO params);

	/** 특정 게시글의 모든 댓글 목록 조회 */
	public List<CommentDTO> selectCommentList(CommentDTO params);

	/** 특정 게시그에 포함된 댓글 개수 조회 */
	public int selectCommentTotalCount(CommentDTO params);

	/** 마이페이지 내 댓글 검색 */
	public List<CommentDTO> selectMyComment(CommentDTO params);
	
	

	public int selectMyCommentCount(CommentDTO params);
}

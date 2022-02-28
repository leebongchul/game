package com.game.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.game.domain.CommentDTO;
import com.game.mapper.CommentMapper;
import com.game.paging.PaginationInfo;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentMapper commentMapper;

	@Override
	public boolean registerComment(CommentDTO params) {
		int queryResult = 0;

		if (params.getCommNum() == null) {
			queryResult = commentMapper.insertComment(params);
		} else {
			queryResult = commentMapper.updateComment(params);
		}

		return (queryResult == 1) ? true : false;
	}

	@Override
	public boolean deleteComment(CommentDTO params) {
		int queryResult = 0;

		/* 내댓글 다중 선택 삭제 시 배열로 반환하여 넘김 */
		String commId = params.getCommNum();
		String[] commidArray = commId.split(",");
		params.setCommNumArr(commidArray);

		queryResult = commentMapper.deleteComment(params);

		return (queryResult == 0) ? false : true;
	}

	@Override
	public List<CommentDTO> getCommentList(CommentDTO params) {
		List<CommentDTO> commentList = Collections.emptyList();
		
		int commTotalCount = commentMapper.selectCommentTotalCount(params);

        PaginationInfo paginationInfo = new PaginationInfo(params);
        paginationInfo.setTotalRecordCount(commTotalCount);
        
        params.setPaginationInfo(paginationInfo);
		
        if (commTotalCount > 0) {
			commentList = commentMapper.selectCommentList(params);
		}

		return commentList;
	}

	@Override
	public List<CommentDTO> selectMyComment(CommentDTO params) {
		List<CommentDTO> commList = Collections.emptyList();

		int commTotalCount = commentMapper.selectMyCommentCount(params);

		PaginationInfo paginationInfo = new PaginationInfo(params);
		paginationInfo.setTotalRecordCount(commTotalCount);

		params.setPaginationInfo(paginationInfo);

		if (commTotalCount > 0) {
			commList = commentMapper.selectMyComment(params);
		}

		return commList;
	}

	@Override
	public CommentDTO selectCommentDetail(String params) {

		CommentDTO comment = commentMapper.selectCommentDetail(params);

		return comment;

	}
}
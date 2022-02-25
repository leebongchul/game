package com.game.service;

import java.util.List;

import com.game.domain.CommentDTO;

public interface CommentService {

	public boolean registerComment(CommentDTO params);

	public boolean deleteComment(CommentDTO params);

	public List<CommentDTO> getCommentList(CommentDTO params);

	public List<CommentDTO> selectMyComment(CommentDTO params);

	public CommentDTO selectCommentDetail(String params);
}
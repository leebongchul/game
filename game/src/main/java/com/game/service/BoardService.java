package com.game.service;

import java.util.List;

import com.game.domain.BoardDTO;
import com.game.domain.ReportDTO;

public interface BoardService {

	public boolean registerBoard(BoardDTO params);

	public BoardDTO getBoardDetail(BoardDTO params);

	public boolean deleteBoard(BoardDTO params);

	public List<BoardDTO> getBoardList(BoardDTO params);

	public List<BoardDTO> searchBoard(BoardDTO params);

	public int plusBoardHit(BoardDTO params);

	public List<ReportDTO> getReportList();

}

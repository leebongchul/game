package com.game.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.game.domain.BoardDTO;
import com.game.domain.ReportDTO;
import com.game.mapper.BoardMapper;
import com.game.paging.PaginationInfo;

@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardMapper boardMapper;

	@Override
	public boolean registerBoard(BoardDTO params) {
		int queryResult = 0;

		if (params.getBoardNum() == null) {
			queryResult = boardMapper.insertBoard(params);
		} else {
			queryResult = boardMapper.updateBoard(params);
		}

		return (queryResult == 1) ? true : false;
	}

	@Override
	public BoardDTO getBoardDetail(BoardDTO params) {
		return boardMapper.selectBoardDetail(params);
	}

	@Override
	public boolean deleteBoard(BoardDTO params) {
		int queryResult = 0;

//		BoardDTO board = boardMapper.selectBoardDetail(params);
//		board.setBoardUpdateId(params.getMemId());

//		if (board != null) {

		/* 내게시글 다중 선택 삭제 시 배열로 반환하여 넘김 */
		String boardId = params.getBoardNum();
		String[] boardidArray = boardId.split(",");
		params.setBoardNumArr(boardidArray);

		queryResult = boardMapper.deleteBoard(params);

		return (queryResult == 0) ? false : true;
	}

	@Override
	public List<BoardDTO> getBoardList(BoardDTO params) {
		List<BoardDTO> boardList = Collections.emptyList();

		int boardTotalCount = boardMapper.selectBoardTotalCount(params);

		PaginationInfo paginationInfo = new PaginationInfo(params);
		paginationInfo.setTotalRecordCount(boardTotalCount);

		params.setPaginationInfo(paginationInfo);

		if (boardTotalCount > 0) {
			boardList = boardMapper.selectBoardList(params);
		}

		return boardList;
	}
	
	@Override
    public List<ReportDTO> getReportList(ReportDTO params) {
	    List<ReportDTO> reportList = Collections.emptyList();
	    
	    int reportTotalCount = boardMapper.reportTotalCount(params);
	    
	    PaginationInfo paginationInfo = new PaginationInfo(params);
        paginationInfo.setTotalRecordCount(reportTotalCount);
	    
        params.setPaginationInfo(paginationInfo);
        
        if (reportTotalCount > 0) {
            reportList = boardMapper.selectReportList(params);
        }
        return reportList;
    }
    
	@Override
	public List<BoardDTO> searchBoard(BoardDTO params) {
		return boardMapper.searchBoard(params);
	}

	@Override
	public int plusBoardHit(BoardDTO params) {
		return boardMapper.plusBoardHit(params);
	}

	

	@Override
	public boolean registerReport(ReportDTO params) {
		int result = boardMapper.insertReport(params);
		if (result == 1) {
			return true;
		} else {
			return false;
		}
	}

}

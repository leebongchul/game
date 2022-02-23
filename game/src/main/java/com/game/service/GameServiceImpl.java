package com.game.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.game.domain.GameScoreDTO;
import com.game.mapper.GameMapper;
import com.game.paging.PaginationInfo;

@Service
public class GameServiceImpl implements GameService {

	@Autowired
	private GameMapper gameMapper;

//	@Override
//	public boolean registerMember(MemberDTO member) {
//		return true;
//	}
//
//	@Override
//	public boolean loginMember(MemberDTO member) {
//		MemberDTO result = gameMapper.selectMember(member);
//
//		return (result == null) ? false : true;
//	}
	
	@Override
    public GameScoreDTO selectGameScore(GameScoreDTO user) {
    
        return gameMapper.selectGameScore(user);
        
    }
	
	@Override
    public int insertGameScore(GameScoreDTO user) {
    
        return gameMapper.insertGameScore(user);
        
    }

	@Override
    public int updateGameScore(GameScoreDTO user) {
    
        return gameMapper.updateGameScore(user);
        
    }
	
	@Override
    public List<GameScoreDTO> selectTop5List(GameScoreDTO user) {
	    
	    List<GameScoreDTO> ranklist = gameMapper.selectTop5List(user);
        
	    return ranklist;
	}
	
	
	
	@Override
    public List<GameScoreDTO> selectGameRankList(GameScoreDTO user) {
        List<GameScoreDTO> rankList = Collections.emptyList();

        int rankTotalCount = gameMapper.selectRankTotalCount(user);

        PaginationInfo paginationInfo = new PaginationInfo(user);
        paginationInfo.setTotalRecordCount(rankTotalCount);

        user.setPaginationInfo(paginationInfo);

        if (rankTotalCount > 0) {
            rankList = gameMapper.selectGameRankList(user);
        }

        return rankList;
    }
	
}

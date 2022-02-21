package com.game.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.game.domain.GameScoreDTO;
import com.game.domain.MemberDTO;
import com.game.mapper.GameMapper;

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
    public List<GameScoreDTO> selectGameRankList(MemberDTO loginMember) {
	    
	    List<GameScoreDTO> ranklist = gameMapper.selectGameRankList(loginMember);
        
	    return ranklist;
	}

}

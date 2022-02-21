package com.game.service;

import java.util.List;

import com.game.domain.GameScoreDTO;
import com.game.domain.MemberDTO;

public interface GameService {

//	public boolean registerMember(MemberDTO member);
//
//	public boolean loginMember(MemberDTO member);

    
    public GameScoreDTO selectGameScore(GameScoreDTO user);
    
    public int insertGameScore(GameScoreDTO user);
    
    public int updateGameScore(GameScoreDTO user);
    
    public List<GameScoreDTO> selectGameRankList(MemberDTO loginMember);
}

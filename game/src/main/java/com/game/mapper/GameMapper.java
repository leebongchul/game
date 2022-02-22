package com.game.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.game.domain.GameScoreDTO;

@Mapper
public interface GameMapper {

//	public int insertMember(MemberDTO params);
//
//	public MemberDTO selectMember(MemberDTO params);
    
    public GameScoreDTO selectGameScore(GameScoreDTO user);
    
    public int insertGameScore(GameScoreDTO user);
    
    public int updateGameScore(GameScoreDTO user);
    
    public List<GameScoreDTO> selectTop5List(GameScoreDTO user);
    
    public List<GameScoreDTO> selectGameRankList(GameScoreDTO user);
    
    public int selectRankTotalCount(GameScoreDTO user);
}

package com.game.domain;

import com.game.paging.Criteria;
import com.game.paging.PaginationInfo;

import lombok.Data;

@Data
public class GameScoreDTO extends Criteria {
    
    private int rno, rankScore, rank;
    private String  memId,gameName,memNick, gameName1,gameName2;
    private PaginationInfo paginationInfo;
    
    
   
    
    
}

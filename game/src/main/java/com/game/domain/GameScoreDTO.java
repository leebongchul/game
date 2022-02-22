package com.game.domain;

import com.game.paging.Criteria;
import com.game.paging.PaginationInfo;

import lombok.Data;

@Data
public class GameScoreDTO extends Criteria {
    
    private int rno, rankScore;
    private String  memId,gameName,memNick;
    private PaginationInfo paginationInfo;
}

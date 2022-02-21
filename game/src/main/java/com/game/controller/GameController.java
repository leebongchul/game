package com.game.controller;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.game.Util.UiUtils;
import com.game.domain.GameScoreDTO;
import com.game.domain.MemberDTO;
import com.game.service.GameService;

@Controller
@RequestMapping("/game/*")
public class GameController extends UiUtils {

    @Autowired
    private GameService gameService;

/**************** GET ***********************************/
    @GetMapping(value = "/test")
    public String testfunction(Model model) {
        return "game/test";
    }
    
    @GetMapping(value = "/rank")
    public String rankfunction(Model model) {
        return "game/rank";
    }

    @GetMapping(value = "/fortress")
    public String fortressopen(Model model) {
        return "game/fortress";
    }

    
    
    @GetMapping(value = "/dino")
    public String dinoopen(@SessionAttribute(name = "loginMem", required = false) MemberDTO loginMember,
            HttpServletRequest request, Model model) {
        
        String gamename = request.getParameter("game");
        GameScoreDTO user = new GameScoreDTO();
        user.setGameName("dino");
        List<GameScoreDTO> top5 = Collections.emptyList();
        
        if(loginMember == null) {
            System.out.println("게스트접속");
            model.addAttribute("member", loginMember);
            top5 =  gameService.selectGameRankList(user);
            model.addAttribute("top5",top5);
            return "game/dino";
        }
        
            System.out.println("유저접속");
            user.setMemId(loginMember.getMemId());
            top5 =  gameService.selectGameRankList(user);
            GameScoreDTO result = gameService.selectGameScore(user); // 유저정보로 rank_table값 불러옴
            
            if(result == null) {                                             // DB에 게임점수 테이블에 정보가 없으면 실행 
                int insertresult = gameService.insertGameScore(user);        // 유저정보로 rank_table에 게임정보 생성
                result = gameService.selectGameScore(user);       // 생성한 정보를 result에 저장
                if(insertresult == 0) {
                    System.out.println("insert 실패 ");
                }
            }
            
         // 세션이 유지되면 로그인 홈으로 이동
            model.addAttribute("member", result);
            model.addAttribute("top5",top5);
            
            return "game/dino";
        
       
    }

    @GetMapping(value = "/ddong")
    public String ddongopen(@SessionAttribute(name = "loginMem", required = false) MemberDTO loginMember,
            HttpServletRequest request, Model model) {
        
        String gamename = request.getParameter("game");
        GameScoreDTO user = new GameScoreDTO();
        user.setGameName("ddong");
        List<GameScoreDTO> top5 = Collections.emptyList();
        
        if(loginMember == null) {
            System.out.println("게스트접속");
            model.addAttribute("member", loginMember);
            top5 =  gameService.selectGameRankList(user);
            model.addAttribute("top5",top5);
            return "game/ddong";
        }
        
            System.out.println("유저접속");
            
            user.setMemId(loginMember.getMemId());
            top5 =  gameService.selectGameRankList(user);
            GameScoreDTO result = gameService.selectGameScore(user); // 유저정보로 rank_table값 불러옴
            
            if(result == null) {                                             // DB에 게임점수 테이블에 정보가 없으면 실행 
                int insertresult = gameService.insertGameScore(user);        // 유저정보로 rank_table에 게임정보 생성
                result = gameService.selectGameScore(user);       // 생성한 정보를 result에 저장
                if(insertresult == 0) {
                    System.out.println("insert 실패 ");
                }
            }
            
         // 세션이 유지되면 로그인 홈으로 이동
            
            model.addAttribute("member", result);
            model.addAttribute("top5",top5);
            return "game/ddong";
        
       
    }
    
    
/*************** POST ************************************/
    
    @PostMapping(value = "/game/ddong")
    @ResponseBody
    public int ddongscoresubmit(@SessionAttribute(name = "loginMem", required = false) MemberDTO loginMember,
            @RequestParam int score) {
        GameScoreDTO user = new GameScoreDTO();
        user.setMemId(loginMember.getMemId());
        user.setRankScore(score);
        user.setGameName("ddong");
        return gameService.updateGameScore(user);
    }
    
    @PostMapping(value = "/game/dino")
    @ResponseBody
    public int dinoscoresubmit(@SessionAttribute(name = "loginMem", required = false) MemberDTO loginMember,
            @RequestParam int score) {
        GameScoreDTO user = new GameScoreDTO();
        user.setMemId(loginMember.getMemId());
        user.setRankScore(score);
        user.setGameName("dino");
        return gameService.updateGameScore(user);
    }

}

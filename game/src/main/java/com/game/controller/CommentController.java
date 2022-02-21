package com.game.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.game.adapter.GsonLocalDateTimeAdapter;
import com.game.domain.CommentDTO;
import com.game.service.CommentService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping(value = "/comments/{boardNum}")
    public JsonObject getCommentList(@PathVariable("boardNum") String str, @ModelAttribute("params") CommentDTO params) {
        
    	System.out.println("params = " +params);
        /**rest 방식으로 입력된 데이터(=여기선 JSON)를 확인하기 쉽게 하기 위한
       rest 클라이언트 프로그램 설치하면 좋긴 할듯 
       https://install.advancedrestclient.com/install 
       나는 저 위 사이트에서 다운 받았음. 블로그에서 다운받으라는 거 해밨는데 안댐. 
       */
        
        //JSON 객체 생성
        JsonObject jsonObj = new JsonObject();
        
        //commentService.getCommentList(params) =
        //특정 게시글에 포함된 댓글의 개수가 1이상이면, 특정 게시글의 모든 댓글 목록을 조회하는 메소드
        List<CommentDTO> commentList = commentService.getCommentList(params);
        
        //commentList의 리턴값이 false라면(=commentList가 null이 아니라면=댓글이 1개 이상 달렸다면)
            if (CollectionUtils.isEmpty(commentList) == false) {
                //댓글의 날짜 표시 처리를 위한 코드
                Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter()).create();
                //Gson클래스의 메소드를 이용해서 commentList에 담긴 댓글들을 JsonArray타입으로 변환
                JsonArray jsonArr = gson.toJsonTree(commentList).getAsJsonArray();
            //JsonArray타입으로 변환된 객체를 다시 JSON객체에 삽입.
            //why? => JSON객체에 담아서 리턴하면 JSON객체가 됨으로써 다양한 데이터 형식을 가질 수 있기 때문.
            //* 한국영화정보진흥원? 작년에 자바스크립트 배울때 거기서 가져와서 실습했던 정보들이 JSON타입이었음.
            jsonObj.add("commentList", jsonArr);
        }

        return jsonObj;
    }

}
package com.game.interceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.game.domain.MemberDTO;
import com.game.service.MemberService;

@Component
public class BlockInterceptor implements HandlerInterceptor {

    @Autowired
    private MemberService memberService;

    // controller로 보내기 전에 처리하는 인터셉터
    // 반환이 false라면 controller로 요청을 안함
    // 매개변수 Object는 핸들러 정보를 의미한다. ( RequestMapping , DefaultServletHandler )
//    @Override
//    public boolean preHandle(HttpServletRequest request,
//                             HttpServletResponse response, Object handler) throws Exception { 

//        }

// // controller의 handler가 끝나면 처리됨

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {

//        HttpSession session =  request.getSession(); //세션값을 session이라느 변수에 저장.
//       MemberDTO sessionid = (MemberDTO)session.getAttribute("loginMem");
//       MemberDTO seallinfo = (MemberDTO)request.getSession().getAttribute("loginMem"); //세션의 전체 정보 키=벨류, 키2=벨류2 형식
        // getParameter = http에서 사용자가 폼을 통해 입력한 memId이라는 name의 value를 가져옴. httpㅌ를통해 입력하지
        // 않은 정보는 못가져옴
//      String sessionmemid = request.getParameter("memId"); 
        MemberDTO dto = (MemberDTO) modelAndView.getModel().get("logininfo");
        HttpSession session = request.getSession(false);

        try {
            if (dto != null) {
                request.getSession().setAttribute("infosession", dto);
                MemberDTO dto2 = (MemberDTO) request.getSession().getAttribute("infosession");

                if (memberService.clearBlock((MemberDTO) dto2) == 3) {
                    // 로그인 차단 대상
                    session.invalidate();
                    response.sendRedirect("/index");
                }
                if (memberService.clearBlock((MemberDTO) dto2) == 1) {
                    // 자동 차단해제 대상

                    session.setAttribute("loginMem", dto2);
                    response.sendRedirect("/index");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
//// view까지 처리가 끝난 후에 처리됨
//    @Override
//    public void afterCompletion(HttpServletRequest request, 
//                                HttpServletResponse response, Object handler, Exception ex) throws Exception {
//
//    }
}

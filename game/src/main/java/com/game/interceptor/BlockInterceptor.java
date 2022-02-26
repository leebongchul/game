package com.game.interceptor;


import java.util.Enumeration;

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
public class BlockInterceptor implements HandlerInterceptor{
 
    // controller로 보내기 전에 처리하는 인터셉터
    // 반환이 false라면 controller로 요청을 안함
    // 매개변수 Object는 핸들러 정보를 의미한다. ( RequestMapping , DefaultServletHandler ) 

//    private Logger logger = LoggerFactory.getLogger(this.getClass());
    

    @Autowired
    private MemberService memberService;

    
   
//    @Override
//    public boolean preHandle(HttpServletRequest request,
//                             HttpServletResponse response, Object handler) throws Exception { 
//        HttpSession session =  request.getSession(); //세션값을 session이라느 변수에 저장.
//        //컨트롤러로 받는 세션값을 도저히 못가져 오기때문에, 다른 클래스에 Static으로 선언해놓은 값에 세션값 저장
//         MemberDTO sessionid = (MemberDTO)session.getAttribute("loginMem");
//
//        if(sessionid == null) {
//            System.out.println(session);
//            System.out.println("세션에 저장된 정보가 없음");
//            return true; 
//        }
//        if(sessionid != null) {
//            System.out.println("세션에 저장된 정보가 있음");
//            return true; 
//        }
//        return true; 
//        return false; 
        
//        System.out.println("작동여부확인!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//        System.out.println(sessionid);
//        return true; 
   //     MemberDTO sessionid = (MemberDTO)session.getAttribute("loginMem");
//        MemberDTO logininfo = (MemberDTO)request.getSession().getAttribute("loginMem");
//        logger.debug("===============================================");
//        logger.debug("==================== BEGIN ====================");

//        MemberDTO member = new MemberDTO(); //로그인한 사용자의 정보를 가져오기 위한 변수
//        MemberDTO result = memberService.selectMember(member);//로그인한 사용자의 모든 정보를 가져옴.
//        HttpSession session = request.getSession(); //세션을 생성.
//        Object obj = session.getAttribute("loginMem");
//        session.setAttribute("loginMem", result); //로그인한 사용자의 모든 정보를 세션에 담음.
//        //로그인한 사용자의 mem_block, mem_id, mem_block_end_date, mem_block_date의 을 세션에 저장.
//        session.setAttribute("memId", result.getMemId()); 
//        session.setAttribute("memBlock", result.getMemBlock());
//        session.setAttribute("memBlockEndDate", result.getMemBlockEndDate());
//        session.setAttribute("memBlockDate", result.getMemBlockDate());
////        String requestURI = request.getRequestURI();
//        //로그인한 사용자의 정보를 저장하는 변수 생성
//        String memId = (String)session.getAttribute("memId");
//        String memBlock = (String)session.getAttribute("memBlock");
//        int  memBlockEndDate= (int)session.getAttribute("memBlockEndDate");
//        int  memBlockDate = (int)session.getAttribute("memBlockDate");
//        MemberDTO memtotalinfo = (MemberDTO)session.getAttribute("loginMem");
////        System.out.println(memtotalinfo);
//       if (   memberService.blockMemberLogin(logininfo) == true) {
//            
//           response.sendRedirect("member/index");
//           return false;
//            
//        };
        
//        }
        
 

        
        
    
    
    
    
// // controller의 handler가 끝나면 처리됨

    @Override
   public void postHandle(HttpServletRequest request, 
                           HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
  
        HttpSession session =  request.getSession(); //세션값을 session이라느 변수에 저장.
//       MemberDTO sessionid = (MemberDTO)session.getAttribute("loginMem");
       MemberDTO sessioninfo = (MemberDTO)request.getSession().getAttribute("loginMem"); //세션의 전체 정보 키=벨류, 키2=벨류2 형식
     //http에서 사용자가 폼을 통해 입력한 memId이라는 name의 value를 가져옴. httpㅌ를통해 입력하지 않은 정보는 못가져옴
       String sessionmemid = request.getParameter("memId"); 
      String sememid = (String)request.getSession().getAttribute("sememid");
      String  sememblock = (String )request.getSession().getAttribute("sememblock");
      String  ssememblockdate = (String )request.getSession().getAttribute("sememblockdate");
      String  sememblockend = (String )request.getSession().getAttribute("sememblockend");
      String sessionblockdate = request.getParameter("memBlockDate");
      String sessionblockend = request.getParameter("memBlockEndDate");
      String sessionblock = request.getParameter("memBlock");
      
      
//       Enumeration names = session.getAttributeNames();
//       while(names.hasMoreElements()) {
//           String name = names.nextElement().toString();
//           String value = session.getAttribute(name).toString();
//           System.out.println(name + " : " + value + "<br>");
//
//       }
       //세션값 정상출력. 로그인한 사용자의 값을 가져옴.
//       session.setAttribute("loginMem", result);
       
                
//        if(sessionid == null) {
//          System.out.println(session);
//         System.out.println("세션에 저장된 정보가 없음");
//        }
           
          if(sessioninfo != null && sememblock.equals("Y")) {
          System.out.println("세션에 저장된 정보가 있음");
          System.out.println((MemberDTO)request.getSession().getAttribute("loginMem"));
          System.out.println(sememid);
          System.out.println(sememblock);
          System.out.println(ssememblockdate);
          System.out.println(sememblockend);
          
          session.invalidate();
          //전체세션 value을 출력
//          System.out.println(sessionmemid);
           
         
          
//          while(names.hasMoreElements()) {
//              String name = names.nextElement().toString();
//              String value = session.getAttribute(name).toString();
//              System.out.println(name + " : " + value + "<br>");
//
//          }
//          System.out.println(memberService.blockMemberLogin(sessioninfo));
//          
         
          
          }
      
      
    }
//// view까지 처리가 끝난 후에 처리됨
//    @Override
//    public void afterCompletion(HttpServletRequest request, 
//                                HttpServletResponse response, Object handler, Exception ex) throws Exception {
//
//    }


}

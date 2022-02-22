/**
 * 
 */
var hpSplit;
var emailSplit;
 $(document).ready(function(){
    hpSplit = $("#hpval").val().split('-'); 
    emailSplit = $("#emailval").val().split('@');
    $("#hp1").val(hpSplit[0]);
    $("#hp2").val(hpSplit[1]);
    $("#hp3").val(hpSplit[2]);
    $("#email1").val(emailSplit[0]);
    $("#email2").val(emailSplit[1]);
});
   
 // 닉네임 유효성검사
 
    $("#nick").blur(function() {
        let check = /^[^@$!%*#?&]{1,10}$/;
        let nick = $('#nick').val();
        console.log(nick); 
        if($('#nickval').val() == nick){
            $("#nickmsg").text("");
            $("#nickcheck").val("Y");
        }
        if($('#nickval').val() != nick){
            $.ajax({
            url : '/mypage/nickcheck.do',
            type : 'post',
            data:{memNick:nick},
            success : function(data) {
                console.log("1 = 중복o / 0 = 중복x : "+ data);                          
                
                if (data == 1) {
                        // 1 : 아이디가 중복되는 문구
                        $("#nickmsg").text("사용중인 닉네임입니다.");
                        $("#nickmsg").css("color","red");
                        $("#nickcheck").val("N");
                } else {
                        
                    if(nick ==""){
                        $("#nickmsg").text("닉네임을 입력해주세요.");
                        $("#nickmsg").css("color","red"); 
                        $("#nickcheck").val("N");
                    }else if(!check.test(nick)){
                        $("#nickmsg").text("1~10자의 한글 또는 영문 또는 숫자를 사용하세요.");
                        $("#nickmsg").css("color","red");
                        $("#nickcheck").val("N");
                    }else{
                        $("#nickmsg").text("");
                        $("#nickcheck").val("Y");
                    }
                        
                }
                
            }, error : function() {
                    console.log("실패");
                }
        });
        }
        
    });
   
   // 핸드폰번호 유효성검사
    function hpCheck() {
        let check = /^[^@$!%*#?&]{1,10}$/;
        let hp = $("#hp1").val() + "-" + $("#hp2").val() + "-" + $("#hp3").val();
        let check1 = /^(?=.*\d)[\d]{3,3}$/;
        let check2 = /^(?=.*\d)[\d]{3,4}$/;
        let check3 = /^(?=.*\d)[\d]{4,4}$/;
        
        if(hpSplit[0] == $("#hp1").val()){            // 로그인중인 본인 번호와 같으면 
            $("#hpmsg").text("");
            $("#hpcheck").val("Y");
        }
        if(hpSplit[1] == $("#hp2").val()){       // 로그인중인 본인 번호와 같으면
            $("#hpmsg").text("");
            $("#hpcheck").val("Y");
        }
        if(hpSplit[2] == $("#hp3").val()){       // 로그인중인 본인 번호와 같으면
            $("#hpmsg").text("");
            $("#hpcheck").val("Y");
        }
        
        if(hpSplit[0] != $("#hp1").val() || hpSplit[1] != $("#hp2").val() || hpSplit[2] != $("#hp3").val()){
            $.ajax({
            url : '/member/hpcheck.do',
            type : 'post',
            data:{memHp:hp},
            success : function(data) {
                console.log("1 = 중복o / 0 = 중복x : "+ data);                          
                
                if (data == 1) {
                        $("#hpmsg").text("사용중인 핸드폰번호입니다.");
                        $("#hpmsg").css("color","red");
                        $("#hpcheck").val("N");
                } else {
                        
                    if($("#hp1").val() == "" || $("#hp2").val() == "" ||$("#hp3").val()== ""){   //또는
                        $("#hpmsg").text("전화번호를 입력해주세요");
                        $("#hpmsg").css("color","red");
                        $("#hpcheck").val("N");
                    }else if(!check1.test($("#hp1").val())){
                        $("#hpmsg").text("전화번호 앞자리는 3자리의 숫자를 사용하세요.");
                        $("#hpmsg").css("color","red");
                        $("#hpcheck").val("N");
                    }else if(!check2.test($("#hp2").val())){
                        $("#hpmsg").text("전화번호 중간자리는 3~4자리의 숫자를 사용하세요");
                        $("#hpmsg").css("color","red");
                        $("#hpcheck").val("N");
                    }else if(!check3.test($("#hp3").val())){
                        $("#hpmsg").text("전화번호 끝자리는 4자리의 숫자를 사용하세요.");
                        $("#hpmsg").css("color","red");
                        $("#hpcheck").val("N");
                    }else{
                        $("#hpmsg").text("");
                        $("#hpcheck").val("Y");
                    }
                        
                }
            }, error : function() {
                    console.log("실패");
                }
        });
        }
            
        
    }
    
 // 이메일 유효성검사
    function emailCheck() {
        let check1 = /^[^ㄱ-ㅎ가-힣]+$/;
        let check2 = /^(.*[a-zA-Z])(?=.*[.])[a-zA-Z\.]+$/;
        let email = $("#email1").val() + "@" + $("#email2").val();
        
        let emailcodecheck = $("#emailcodecheck").val();
        let btn_check = $("#btn_check");
        let code = $("#code");
        //이메일 변경 시 이메일 중복체크, 이메일 인증체크 값 'N'으로 변경
        emailcheck = 'N';
        emailcodecheck = 'N';
        //카카오톡으로 회원가입 시 계정의 이메일이 자동 입력되고 이메일의 유효성체크와 인증체크를 진행하지 않아도 됨(버튼과 코드입력란이 안보임)
        //하지만 이메일을 수정할 경우 인증체크를 진행해야함-> 버튼과 코드입력란이 보이게 바뀜
        btn_check.replaceWith($('<button type="button" class="btn_check" id="btn_check" onclick="sendEmailCode()">인증번호 받기</button>'));
        code.replaceWith($('<input type ="text" class="code" placeholder="인증번호를 입력해주세요" id="code" onblur="emailCodeCheck()" autocomplete="off">'));
        
        if(emailSplit[0] == $("#email1").val()){            // 로그인중인 본인 이메일과 같으면 
            $("#emailmsg").text("");
            $("#emailcheck").val("Y");
        }
        if(emailSplit[1] == $("#email2").val()){       // 로그인중인 본인 이메일과 같으면
            $("#emailmsg").text("");
            $("#emailcheck").val("Y");
        }
        if(emailSplit[0] != $("#email1").val() || emailSplit[1] != $("#email2").val()){
            
            $.ajax({
                url : '/member/emailcheck.do',
                type : 'post',
                data:{memEmail:email},
                success : function(data) {
                    console.log("1 = 중복o / 0 = 중복x : "+ data);                          
                    
                    if (data == 1) {
                            // 1 : 아이디가 중복되는 문구
                            $("#emailmsg").text("사용중인 이메일입니다.");
                            $("#emailmsg").css("color","red");
                            $("#emailcheck").val("N");
                    } else {
                            
                        if($("#email1").val() == "" || $("#email2").val() == ""){
                            $("#emailmsg").text("이메일을 입력해주세요");
                            $("#emailmsg").css("color","red");
                            $("#emailcheck").val("N");
                        }else if(!check1.test($("#email1").val())){
                            $("#emailmsg").text("한글은 사용 불가능합니다");
                            $("#emailmsg").css("color","red");
                            $("#emailcheck").val("N");
                        }else if(!check2.test($('#email2').val())){
                            $("#emailmsg").text("이메일 형식을 확인해주세요.");
                            $("#emailmsg").css("color","red");
                            $("#emailcheck").val("N");
                        }else if($("#email1").val() != "" && $("email2").val() != ""){
                            $("#emailmsg").text("");
                            $("#emailcheck").val("Y");
                        }else{
                            $("#emailmsg").text("");
                            $("#emailcheck").val("Y");
                        }
                            
                    }
                    
                }, error : function() {
                        console.log("실패");
                    }
            });   
        }
        
 }
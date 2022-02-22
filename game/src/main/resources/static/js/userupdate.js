/**
 * 
 */
 
 function userUpdate(){
	var nick, tel, hp1, hp2, hp3, email1, email2;
	
	let nickcheck = document.getElementById("nickcheck");
    let hpcheck = document.getElementById("hpcheck");
    let emailcheck = document.getElementById("emailcheck");
	nick = document.getElementById("nick");
	tel = document.getElementById("tel");
	hp1 = document.getElementById("hp1");
	hp2 = document.getElementById("hp2");
	hp3 = document.getElementById("hp3");
	email1 = document.getElementById("email1");
	email2 = document.getElementById("email2");
	document.getElementById("memHp").value = hp1.value+"-"+ hp2.value +"-"+ hp3.value;
    document.getElementById("memEmail").value = email1.value + "@" + email2.value;
    
    if(!textCheck_nick(nick)) {
        alert("닉네임을 확인해주세요.");
        return false;
    }else if(tel.value == "choice"){
		alert("통신사를 선택해주세요.");
        return false;
    }else if (!textCheck_hp()) {
        alert("핸드폰번호를 확인해주세요.");
        return false;
   	}else if (!textCheck_email()) {
        alert("이메일을 확인해주세요.");
        return false;
        /*
    }else if(nickcheck.value == 'N'){
		alert("닉네임 중복을 확인해주세요");
        return false;
    } else if (hpcheck.value == 'N') {
        alert("핸드폰번호 중복을 확인해주세요");
        return false;
    } else if (emailcheck.value == 'N') {
        alert("이메일 중복을 확인해주세요");
        return false;   
        */
    }else{
		return true;
	}
        
        
        /*
    if(nick.value == ""){
		alert("닉네임을 입력해주세요.");
		return false;
	}else if(hp.value == ""){
		
	}
	*/
    
}
 
 //닉네임
function textCheck_nick(obj) {
    let nickmsg = document.querySelector("#nickmsg");
    let str = obj.value;
    let check = /^[^@$!%*#?&]{1,10}$/;

    if (obj.value == "") {
        nickmsg.innerHTML = "닉네임을 입력해주세요.";
        nickmsg.style.color = "red";
        return false;
    } else if (!check.test(str)) {
        nickmsg.innerHTML = "1~10자의 한글 또는 영문 또는 숫자를 사용하세요.";
        nickmsg.style.color = "red";
        return false;
    } else {
        nickmsg.innerHTML = "";
        return true; // 확인이 완료 되었을 때
    }
}
//전화번호
function textCheck_hp() {
    let hp1, hp2, hp3;
    let hpmsg = document.querySelector("#hpmsg");

    hp1 = document.getElementById("hp1");
    hp2 = document.getElementById("hp2");
    hp3 = document.getElementById("hp3");
    let str1 = hp1.value;
    let str2 = hp2.value;
    let str3 = hp3.value;
    let check1 = /^(?=.*\d)[\d]{3,3}$/;
    let check2 = /^(?=.*\d)[\d]{3,4}$/;
    let check3 = /^(?=.*\d)[\d]{4,4}$/;

    if (hp1.value == "" || hp2.value == "" || hp3.value == "") {   //또는
        hpmsg.innerHTML = "전화번호를 입력해주세요.";
        hpmsg.style.color = "red";
        return false;
    } else if (!check1.test(str1)) {
        hpmsg.innerHTML = "전화번호 앞자리는 3자리의 숫자를 사용하세요.";
        hpmsg.style.color = "red";
        return false;
    } else if (!check2.test(str2)) {
        hpmsg.innerHTML = "전화번호 중간자리는 3~4자리의 숫자를 사용하세요.";
        hpmsg.style.color = "red";
        return false;
    } else if (!check3.test(str3)) {
        hpmsg.innerHTML = "전화번호 끝자리는 4자리의 숫자를 사용하세요.";
        hpmsg.style.color = "red";
        return false;
    } else {
        hpmsg.innerHTML = "";
        return true; // 확인이 완료 되었을 때
    }
}
//이메일
function textCheck_email() {
    let email1, email2, email3;
    let emailmsg = document.querySelector("#emailmsg");

    email1 = document.getElementById("email1");
    email2 = document.getElementById("email2");
    email3 = document.getElementById("email3");

    let str1 = email1.value;
    let str2 = email2.value;
    var check1 = /^[^ㄱ-ㅎ가-힣]+$/;
    let check2 = /^(.*[a-zA-Z])(?=.*[.])[a-zA-Z\.]+$/;

    if (email1.value == "" || email2.value == "") {
        emailmsg.innerHTML = "이메일을 입력해주세요.";
        emailmsg.style.color = "red";
        return false;
    } else if (!check1.test(str1)) {
        emailmsg.innerHTML = "한글은 사용 불가능합니다.";   //영문,숫자 모두 사용하여 조합
        emailmsg.style.color = "red";
        return false;
    } else if (!check2.test(str2)) {
        emailmsg.innerHTML = "이메일 형식을 확인해주세요.";
        emailmsg.style.color = "red";
        return false;

    } else if (email1.value !== "" && email2.value !== "") {
        emailmsg.innerHTML = "";
        return true; // 확인이 완료 되었을 때
    }

}
 
 
 
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
        
        //let emailcodecheck = $("#emailcodecheck").val();
        let btn_check = $("#btn_check");
        let code = $("#code");
        //이메일 변경 시 이메일 중복체크, 이메일 인증체크 값 'N'으로 변경
        emailcheck = 'N';
        //emailcodecheck = 'N';
        //카카오톡으로 회원가입 시 계정의 이메일이 자동 입력되고 이메일의 유효성체크와 인증체크를 진행하지 않아도 됨(버튼과 코드입력란이 안보임)
        //하지만 이메일을 수정할 경우 인증체크를 진행해야함-> 버튼과 코드입력란이 보이게 바뀜
        btn_check.replaceWith($('<button type="button" class="btn_check" id="btn_check" onclick="sendEmailCode()">인증번호 받기</button>'));
        //code.replaceWith($('<input type ="text" class="code" placeholder="인증번호를 입력해주세요" id="code" onblur="emailCodeCheck()" autocomplete="off">'));
        
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
        
        
        
  function emailSend() {
    let email2, email3, emailmsg;
    email2 = document.getElementById("email2");
    email3 = document.getElementById("email3");
    emailmsg = document.getElementById("emailmsg");
    emailcheck = document.getElementById("emailcheck");
    //emailcodecheck = document.getElementById("emailcodecheck");

    emailcheck = 'N';
    //emailcodecheck = 'N';

   	email2.value = email3.value;
    //        let check1 = /^[^ㄱ-ㅎ가-힣]+$/;

    if (email3.value == "self") {
        //alert("이메일 주소를 입력해주세요.");
        email2.value = "";
        email2.disabled = false;
    } else if (email2.value == email3.value) {
        emailmsg.innerHTML = "";
        email2.disabled = true;
    }
  }
        
 
 }
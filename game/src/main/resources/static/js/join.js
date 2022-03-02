/***
 * 회원가입 폼 체크(공백체크, 정규식체크)
 ***/
 $(function() {
    console.log( "ready!" );
    $("#nick").trigger("blur");
});
 
 // 아이디 유효성 검사(1 = 중복 / 0 != 중복)
    $("#id").blur(function() {
        let check = /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d]{6,12}$/;
        let id = $('#id').val();
        $.ajax({
            url : '/member/idcheck.do',
            type : 'post',
            data:{memId:id},
            success : function(data) {
                console.log("1 = 아이디중복o / 0 = 중복x : "+ data);                          
                
                if (data == 1) {
                        // 1 : 아이디가 중복되는 문구
                        $("#idmsg").text("사용중인 아이디입니다");
                        $("#idmsg").css("color","red");
                        $("#idcheck").val("N");
                        
                } else {
                        
                    if( id == ""){
                        $("#idmsg").text("아이디를 입력해주세요");
                        $("#idmsg").css("color","red");
                        $("#idcheck").val("N");
                    }else if(!check.test(id)){
                        $("#idmsg").text("6~12자 영문, 숫자를 조합하세요");
                        $("#idmsg").css("color","red");
                        $("#idcheck").val("N");
                    }else{
                        $("#idmsg").text("");
                        $("#idcheck").val("Y");
                    }
                        
                }
                
            }, error : function() {
                    console.log("실패");
                }
        });
    });
// 닉네임 유효성검사
    $("#nick").blur(function() {
        let check = /^[^@$!%*#?&]{1,10}$/;
        let nick = $('#nick').val();
        $.ajax({
            url : '/member/nickcheck.do',
            type : 'post',
            data:{memNick:nick},
            success : function(data) {
                console.log("1 = 중복o / 0 = 중복x : "+ data);                          
                
                if (data == 1) {
                        // 1 : 아이디가 중복되는 문구
                        $("#nickmsg").text("사용중인 닉네임입니다");
                        $("#nickmsg").css("color","red");
                        $("#nickcheck").val("N");
                } else {
                        
                    if(nick ==""){
                        $("#nickmsg").text("닉네임을 입력해주세요");
                        $("#nickmsg").css("color","red");
                        $("#nickcheck").val("N");
                    }else if(!check.test(nick)){
                        $("#nickmsg").text("1~10자의 한글 또는 영문 또는 숫자를 사용하세요");
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
    });
    
    // 핸드폰번호 유효성검사
    
    function hpCheck() {
        let check = /^[^@$!%*#?&]{1,10}$/;
        let hp = $("#hp1").val() + "-" + $("#hp2").val() + "-" + $("#hp3").val();
        let check1 = /^(?=.*\d)[\d]{3,3}$/;
        let check2 = /^(?=.*\d)[\d]{3,4}$/;
        let check3 = /^(?=.*\d)[\d]{4,4}$/;
        $.ajax({
            url : '/member/hpcheck.do',
            type : 'post',
            data:{memHp:hp},
            success : function(data) {
                console.log("1 = 중복o / 0 = 중복x : "+ data);                          
                
                if (data == 1) {
                        // 1 : 아이디가 중복되는 문구
                        $("#hpmsg").text("사용중인 핸드폰번호입니다");
                        $("#hpmsg").css("color","red");
                        $("#hpcheck").val("N");
                } else {
                        
                    if($("#hp1").val() == "" || $("#hp2").val() == "" ||$("#hp3").val()== ""){   //또는
                        $("#hpmsg").text("전화번호를 입력해주세요");
                        $("#hpmsg").css("color","red");
                        $("#hpcheck").val("N");
                    }else if(!check1.test($("#hp1").val())){
                        $("#hpmsg").text("전화번호 앞자리는 3자리의 숫자를 사용하세요");
                        $("#hpmsg").css("color","red");
                        $("#hpcheck").val("N");
                    }else if(!check2.test($("#hp2").val())){
                        $("#hpmsg").text("전화번호 중간자리는 3~4자리의 숫자를 사용하세요");
                        $("#hpmsg").css("color","red");
                        $("#hpcheck").val("N");
                    }else if(!check3.test($("#hp3").val())){
                        $("#hpmsg").text("전화번호 끝자리는 4자리의 숫자를 사용하세요");
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
        
        
        $.ajax({
            url : '/member/emailcheck.do',
            type : 'post',
            data:{memEmail:email},
            success : function(data) {
                console.log("1 = 중복o / 0 = 중복x : "+ data);                          
                
                if (data == 1) {
                        // 1 : 아이디가 중복되는 문구
                        $("#emailmsg").text("사용중인 이메일입니다");
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
                        $("#emailmsg").text("이메일 형식을 확인해주세요");
                        $("#emailmsg").css("color","red");
                        $("#emailcheck").val("N");
                    }else if($("#email1").val() !== "" && $("email2").val() !== ""){
                        $("#emailmsg").text("이메일 인증해주세요");
                        $("#emailcheck").val("Y");
                    }else{
                        $("#emailmsg").text("이메일 인증해주세요");
                        $("#emailcheck").val("Y");
                    }
                        
                }
                
            }, error : function() {
                    console.log("실패");
                }
        });
    }
    
 // 이메일 내용 변경 시
    function emailUpdateCheck() {
        let check1 = /^[^ㄱ-ㅎ가-힣]+$/;
        let check2 = /^(.*[a-zA-Z])(?=.*[.])[a-zA-Z\.]+$/;
        let email = $("#email1").val() + "@" + $("#email2").val();
        
        
        $.ajax({
            url : '/member/emailcheck.do',
            type : 'post',
            data:{memEmail:email},
            success : function(data) {
                console.log("1 = 중복o / 0 = 중복x : "+ data);                          
                
                if (data == 1) {
                        // 1 : 아이디가 중복되는 문구
                        $("#emailmsg").text("사용중인 이메일입니다");
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
                        $("#emailmsg").text("이메일 형식을 확인해주세요");
                        $("#emailmsg").css("color","red");
                        $("#emailcheck").val("N");
                    }else if($("#email1").val() !== "" && $("email2").val() !== ""){
                        $("#emailmsg").text("이메일 인증해주세요");
                        $("#emailcheck").val("Y");
                    }else{
                        $("#emailmsg").text("이메일 인증해주세요");
                        $("#emailcheck").val("Y");
                    }
                        
                }
                
            }, error : function() {
                    console.log("실패");
                }
        });
    }
    
    // 이메일 인증코드 전송
    function sendEmailCode() {
        let email = $("#email1").val() + "@" + $("#email2").val();
        $.ajax({
            url : '/member/sendEmail',
            type : 'post',
            data:{memEmail:email},
            success : function(data) {
                alert("인증번호가 발송되었습니다");
            }, error : function() {
                    console.log("실패");
                }
        });
    };
    
 // 입력된 이메일 인증코드 유효성검사
 
    function emailCodeCheck() {
        let code = $("#code").val();
        
        $.ajax({
            url : '/member/verifyCode',
            type : 'post',
            data:{code : code},
            success : function(data) {
                console.log("1 = 일치 / 0 = 불일치 : "+ data);                          
                
                if (data == 1) {
                        // 1 : 인증코드 일치 시
                        $("#emailmsg").text("");
                        $("#emailmsg").css("color","red");
                        $("#emailcodecheck").val("Y");
                        console.log(data);
                } else {
                        
                    if($("#code").val() == ""){
                        $("#emailmsg").text("인증코드를 입력해주세요");
                        $("#emailmsg").css("color","red");
                        $("#emailcodecheck").val("N");
                        console.log(data);
                    }else {
                        $("#emailmsg").text("인증코드가 일치하지 않습니다");
                        $("#emailmsg").css("color","red");
                        $("#emailcheck").val("N");
                        console.log(data);
                    }
                        
                }
                
            }, error : function() {
                    console.log("실패");
                }
        });
    }
 
// 폼 서밋
function formsubmit(form) {
    let idcheck = document.getElementById("idcheck");
    let nickcheck = document.getElementById("nickcheck");
    let hpcheck = document.getElementById("hpcheck");
    let emailcheck = document.getElementById("emailcheck");
    let emailcodecheck = document.getElementById("emailcodecheck");
    let hp1 = document.getElementById("hp1");
    let hp2 = document.getElementById("hp2");
    let hp3 = document.getElementById("hp3");
    let email1 = document.getElementById("email1");
    let email2 = document.getElementById("email2");
    document.getElementById("memHp").value = hp1.value+"-"+ hp2.value +"-"+ hp3.value;
    document.getElementById("memEmail").value = email1.value + "@" + email2.value;

    if (!textCheck_id(form.id)) {
        alert("ID를 확인해주세요");
        return false;
    } else if (!textCheck_pass(form.pass)) {
        alert("비밀번호를 확인해주세요");
        return false;
    } else if (!cpassCheck()) {
        alert("비밀번호 확인을 확인해주세요");
        return false;
    } else if (!textCheck_name(form.name)) {
        alert("이름을 확인해주세요");
        return false;
    } else if (!textCheck_nick(form.nick)) {
        alert("닉네임을 확인해주세요");
        return false;
    } else if (!textCheck_hp()) {
        alert("핸드폰번호를 확인해주세요");
        return false;
    } else if (!textCheck_email()) {
        alert("이메일을 확인해주세요");
        return false;
    } else if (idcheck.value == 'N') {
        alert("아이디 중복을 확인해주세요");
        return false;
    } else if (nickcheck.value == 'N') {
        alert("닉네임 중복을 확인해주세요");
        return false;
    } else if (hpcheck.value == 'N') {
        alert("핸드폰번호 중복을 확인해주세요");
        return false;
    } else if (emailcheck.value == 'N') {
        alert("이메일 중복을 확인해주세요");
        return false;
    } /*
    else if (emailcodecheck.value == 'N') {
        alert("이메일 인증을 진행해주세요");
        return false;
    } 
    */
    else {
        return true;
    }
}
//아이디
function textCheck_id(obj) {

    let idmsg = document.querySelector("#idmsg");
    let kakaoJoin = document.getElementById("kakaoJoin");

    //let check = /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d]{6,12}$/;
    let check = /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d]{6,12}$/;
    if (obj.value == "" && kakaoJoin.value == 'N') {
        idmsg.innerHTML = "회원ID를 입력해주세요";
        idmsg.style.color = "red";
        return false;
    } else if (!check.test(obj.value) && kakaoJoin.value == 'N') {
        idmsg.innerHTML = "6~12자 영문, 숫자를 조합하세요";
        idmsg.style.color = "red";
        return false;
    } else {
        idmsg.innerHTML = "";
        return true; // 확인이 완료 되었을 때
    }
}

//비밀번호
function textCheck_pass(obj) {
    let passmsg = document.querySelector("#passmsg");
    let pass = document.getElementById("pass");
    let cpass = document.getElementById("cpass");
    let cpassmsg = document.getElementById("cpassmsg");

    //let check = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,16}$/;
    let check = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,16}$/;

    if (obj.value == "") {
        passmsg.innerHTML = "비밀번호를 입력해주세요";
        passmsg.style.color = "red";
        return false;
    } else if (!check.test(obj.value)) {
        passmsg.innerHTML = "8~16자 영문, 숫자, 특수문자를 사용하세요";
        passmsg.style.color = "red";
        return false;
    } else {
        passmsg.innerHTML = "";
        return true;
    }

    if (pass.value != "" && cpass.value != "") {
        if (pass.value != cpass.value) {
            cpassmsg.innerHTML = "비밀번호가 다릅니다";
            cpassmsg.style.color = "red";
            return false;
        } else {
            cpassmsg.innerHTML = "";
            return true;
        }
    }

}

/****************************
회원가입 - 비밀번호, 비밀번호 확인 체크
****************************/
function cpassCheck() {
    let pass, cpass, cpassmsg;
    pass = document.getElementById("pass");
    cpass = document.getElementById("cpass");
    cpassmsg = document.getElementById("cpassmsg");

    if (pass.value != "" && cpass.value != "") {
        if (pass.value != cpass.value) {
            cpassmsg.innerHTML = "비밀번호가 다릅니다";
            cpassmsg.style.color = "red";
            return false;
        } else {
            cpassmsg.innerHTML = "";
            return true;
        }
    } else if (cpass.value == "") {
        cpassmsg.innerHTML = "비밀번호 확인을 입력해주세요";
        cpassmsg.style.color = "red";
        return false;
    } else if (cpass.value !== "" || pass.value == cpass.value) {
        cpassmsg.innerHTML = "";
        return true;
    }

}

//이름
function textCheck_name(obj) {
    let namemsg = document.querySelector("#namemsg");
    let str = obj.value;
    let check = /^[ㄱ-ㅎ가-힣a-zA-Z]+$/;

    if (obj.value == "") {
        namemsg.innerHTML = "이름을 입력해주세요";
        namemsg.style.color = "red";
        return false;
    } else if (!check.test(str)) {
        namemsg.innerHTML = "한글 또는 영문을 사용하세요";
        namemsg.style.color = "red";
        return false;
    } else {
        namemsg.innerHTML = "";
        return true; // 확인이 완료 되었을 때
    }
}

/*function nickchecksave(obj){
    let check_save1 = document.getElementById("check_save1");
    check_save1.value = obj.value;
}*/
//닉네임
function textCheck_nick(obj) {
    let nickmsg = document.querySelector("#nickmsg");
    let str = obj.value;
    let check = /^[^@$!%*#?&]{1,10}$/;

    if (obj.value == "") {
        nickmsg.innerHTML = "닉네임을 입력해주세요";
        nickmsg.style.color = "red";
        return false;
    } else if (!check.test(str)) {
        nickmsg.innerHTML = "1~10자의 한글 또는 영문 또는 숫자를 사용하세요";
        nickmsg.style.color = "red";
        return false;
    } else {
        nickmsg.innerHTML = "";
        return true; // 확인이 완료 되었을 때
    }
}

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
        hpmsg.innerHTML = "전화번호를 입력해주세요";
        hpmsg.style.color = "red";
        return false;
    } else if (!check1.test(str1)) {
        hpmsg.innerHTML = "전화번호 앞자리는 3자리의 숫자를 사용하세요";
        hpmsg.style.color = "red";
        return false;
    } else if (!check2.test(str2)) {
        hpmsg.innerHTML = "전화번호 중간자리는 3~4자리의 숫자를 사용하세요";
        hpmsg.style.color = "red";
        return false;
    } else if (!check3.test(str3)) {
        hpmsg.innerHTML = "전화번호 끝자리는 4자리의 숫자를 사용하세요";
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
        emailmsg.innerHTML = "이메일을 입력해주세요";
        emailmsg.style.color = "red";
        return false;
    } else if (!check1.test(str1)) {
        emailmsg.innerHTML = "한글은 사용 불가능합니다";   //영문,숫자 모두 사용하여 조합
        emailmsg.style.color = "red";
        return false;
    } else if (!check2.test(str2)) {
        emailmsg.innerHTML = "이메일 형식을 확인해주세요";
        emailmsg.style.color = "red";
        return false;

    } else if (email1.value !== "" && email2.value !== "") {
        emailmsg.innerHTML = "";
        return true; // 확인이 완료 되었을 때
    }

}

//인증번호
//function numberCheck_email(obj){
//    let number;
//    let emailmsg = document.querySelector("#emailmsg");
//    
//    number = document.getElementById("number");
//    
//    let str1 = number.value;
//    var check1 = /^[\d]+$/;
//    
//    if(number.value == ""){
//        emailmsg.innerHTML = "인증번호를 입력해주세요.";
//        emailmsg.style.color = "red";
//        return false;
//    }else if(!check1.test(str1)){
//        emailmsg.innerHTML = "인증번호는 숫자만 입력 가능합니다.";
//        emailmsg.style.color = "red";
//    }else if(number.value !== ""){
//        emailmsg.innerHTML = "";
//        
//    }
//    
//}


function emailSend() {
    let email1, email2, email3, emailmsg;
    email1 = document.getElementById("email1");
    email2 = document.getElementById("email2");
    email3 = document.getElementById("email3");
    emailmsg = document.getElementById("emailmsg");
    emailcheck = document.getElementById("emailcheck");
    emailcodecheck = document.getElementById("emailcodecheck");

    emailcheck = 'N';
    emailcodecheck = 'N';

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

    //        if(email1.value == "" || email2.value == ""){
    //            emailmsg.innerHTML = "이메일을 입력해주세요.";
    //            emailmsg.style.color = "red";
    //            emailcheck.value = 'N';
    //        }else if(!check1.test(email1.value)){
    //             emailmsg.innerHTML = "한글은 사용 불가능합니다.";
    //            emailmsg.style.color = "red";
    //            emailcheck.value = 'N';
    //        }else if(email1.value !== "" && email2.value !== ""){
    //            emailmsg.innerHTML = "";
    //            emailcodecheck = 'N'
    //        }else{
    //            emailmsg.innerHTML = "";
    //            emailcheck.value = 'Y';
    //            
    //        }

}

function emailUpdateCheck() {
    //*let emailcodecheck = document.getElementById("emailcodecheck").value;
    //*let btn_check = document.getElementById("btn_check");
    //*let code = document.getElementById("code");
    //이메일 변경 시 이메일 중복체크, 이메일 인증체크 값 'N'으로 변경
    //emailcheck = 'N';
    emailcodecheck = 'N';
    //카카오톡으로 회원가입 시 계정의 이메일이 자동 입력되고 이메일의 유효성체크와 인증체크를 진행하지 않아도 됨(버튼과 코드입력란이 안보임)
    //하지만 이메일을 수정할 경우 인증체크를 진행해야함-> 버튼과 코드입력란이 보이게 바뀜
    //*btn_check.replaceWith($('<button type="button" class="btn_check" id="btn_check" onclick="sendEmailCode()">인증번호 받기</button>'));
    //*code.replaceWith($('<input type ="text" class="code" placeholder="인증번호를 입력해주세요" id="code" onblur="emailCodeCheck()" autocomplete="off">'));
    alert("j");

}








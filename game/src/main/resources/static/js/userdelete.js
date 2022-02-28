//아이디
function textCheck_id(obj){
    
    let idmsg = document.querySelector("#idmsg");
    let str = obj.value;
    //let check = /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d]{6,12}$/;
    let check = /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d]{6,12}$/;
    
    if(obj.value ==""){
        idmsg.innerHTML = "아이디를 입력해주세요";
        idmsg.style.color = "red";
        return false;
    }else if(!check.test(str)){
        idmsg.innerHTML = "6~12자 영문, 숫자를 조합하세요";
        idmsg.style.color = "red";
    }else{
        idmsg.innerHTML = "";
    }
}

//비밀번호
function textCheck_pass(obj){
    let passmsg = document.querySelector("#passmsg");
    let pass = document.getElementById("pass");
    let cpass = document.getElementById("cpass");
    let cpassmsg = document.getElementById("cpassmsg");
    let str = obj.value;
    //let check = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,16}$/;
    let check = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,16}$/;
    
    if(obj.value ==""){
        passmsg.innerHTML = "비밀번호를 입력해주세요";
        passmsg.style.color = "red";
    }else if(!check.test(str)){
        passmsg.innerHTML = "8~16자 영문, 숫자, 특수문자를 사용하세요";
        passmsg.style.color = "red";
    }else{
        passmsg.innerHTML = "";
    }
    
    if(pass.value != "" && cpass.value != ""){
        if(pass.value != cpass.value){
            cpassmsg.innerHTML = "비밀번호가 다릅니다";
            cpassmsg.style.color = "red";
            return false;
        }else{
            cpassmsg.innerHTML = "";
            return true;
        }
    }
    
}   
function checkCount(pname){
		let list,count;
		
		list = document.getElementsByName(pname);
		count = 0;
		
		for(let i=0;i<list.length;i++){
			if(list[i].checked){
				count++;
			}
		}
		return count;
	}        

function userDeleteCheck(){
        let loginId, loginPass;
        
        loginId = document.getElementById("loginId");
        loginPass = document.getElementById("loginPass");
        if(loginId.value == ""){
            alert("아이디를 입력해주세요");
            return false;
        }else if(loginPass.value == ""){
            alert("비밀번호를 입력해주세요");
            return false;
        }else if(checkCount("agree") == 0){
        	alert("탈퇴 동의란을 확인해주세요");
            return false;
        }else{
            userdeleteForm.submit();
        } 
        
}
    
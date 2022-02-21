var canvas = document.createElement("canvas");

var ctx = canvas.getContext("2d");
var now_score = document.getElementById("now_score");
let max_score = document.getElementById("max_score");
let guest = document.getElementById("guest");


var grid = {
width: 800,
height:450

}



////var player ={

////};

var rock = [];

var score=0;

var key ={};

var frame =0;

var 게임진행 = false;

//*스마일 그려주는 코드https://developer.mozilla.org/ko/docs/Web/API/Canvas_API/Tutorial/Drawing_shapes


//나
////function drawPlayer(){

    ////ctx.beginPath();

    ////ctx.fillStyle = "red";

    ////ctx.arc(player.x, player.y, 25 ,0,Math.PI*2,true); //원/arc(x, y, r, st, ed, anticlockwise) :x,y원의 중심점/r반지름/st,ed시작각과 끝각(3시방향 0도,6시방향 90도,9시방향180도/anticlockwise방향지정(true반시계) )
    //라디안 = 각도 * Math.PI/180 --> 0,Math.PI*2 0도부터 시작해서 360도까지 돈다

    ////ctx.stroke(); //선의 종류를 지정해주는 함수 (한 번의 붓질로 선을 그림)
////}

var rupee = new Image();
rupee.src = '../image/루피다.PNG';

var player = {
  x : 390,
  y : 395,
  width : 30, 
  height : 45,
  draw(){
    //ctx.fillStyle = 'green';
    //ctx.fillRect(this.x , this.y, this.width, this.height);
    ctx.drawImage(rupee, this.x-6, this.y-5,40,60);
  }
}



var 게임시작 = {
  x : 250,
  y : 200,
  draw(){
    ctx.font = "17pt Fira";
    ctx.fillStyle = 'black';
    ctx.fillText("스페이스바를 누르면 게임시작",this.x,this.y);
  }
}

var 게임오버 = {
  x : 310,
  y : 220,
  draw(){
    ctx.font = "50px Arial";
    ctx.fillStyle = 'black';
    ctx.fillText("게임오버" , this.x, this.y);
    
  }
}
var 재시작 = {
  x : 220,
  y : 250,
  draw(){
    ctx.font = "25px Arial";
    ctx.fillStyle = 'black';
    ctx.fillText("스페이스바를 누르면 재시작합니다." , this.x, this.y);
    
  }
}
var ddong = new Image();
ddong.src = '../image/똥확정.PNG';

//떨어지는 장애물
function drawRock(){

  for(var i=0 ; i < rock.count ; i++){

    //ctx.fillStyle = "blue";
    //ctx.fillRect(rock[i].x, rock[i].y ,30,30);
    ctx.drawImage(ddong, rock[i].x, rock[i].y,40,40);
  }

}



//장애물 생성
function makeRock(){

  for(var i=0 ; i < rock.count ; i++){

    if(rock[i].isExisted !=true){   //isExisted : 배열 요소값이 존재하는지 여부

      rock[i].isExisted =true;

      rock[i].x = Math.random()*800; //난수생성하고, 곱해서 장애물이 떨어지는 범위지정

      rock[i].y = Math.random()*50;

      rock[i].frame = (Math.random()*5)%8+2; //Math.random():0~1사이랜덤/*10-->0~9사이랜8

    }

  }

}



//캔버스 배경
function draw(){

  ctx.fillStyle = "white";

  ctx.fillRect(0,0,grid.width, grid.height);

  ////drawPlayer();
  player.draw();

  makeRock();

  drawRock();
  //점수
  ctx.fillStyle = "black";  //점수 색깔

  ctx.font = "25px Arial";

  ctx.fillText("Score: " + score, 10, 30);

}

function tempFunc(){
    console.log("인풋바뀜");
}


function updatePosition(){

  if(key[37] && player.x > 51)

  player.x -=5 ;

  if(key[39] && player.x < 748)

  player.x +=5;


  for(var i=0 ; i < rock.count ; i++){

      if(rock[i].isExisted){

        rock[i].y += rock[i].frame;

        if(rock[i].y > 760)

        rock[i].isExisted=false;


        //게임종료
        if( Math.abs(rock[i].x -player.x) <40 && Math.abs(rock[i].y -player.y)<40){
            if(게임진행 = true){
                clearInterval(interval);
                게임진행 = false;
                now_score.value = score;
                
                // 신기록 갱신
                if(parseInt(now_score.value) > parseInt(max_score.value)){
                    // 현재점수가 최고점수보다 높다면
                   max_score.value = now_score.value; // 현재점수를 최고점수에 저장
                    
                    if(guest.value == 'false'){ // 게스트계정이 아닌경우 최고점수 갱신
                        
                       $.ajax({                 // DB에 최고점수 업데이트
                         url : '/game/ddong',
                         type : 'post',
                         data:{score : parseInt(max_score.value)},
                         success : function(data) {
                             console.log("1 = 성공 / 0 = 실패 : "+ data);   
 
                         }, 
                         error : function() {
                                 console.log("실패");
                         }
                       });
                       
                    }
                    
                }
                
                게임오버.draw();
                재시작.draw();
            }
        }

        //score -=5;

      }

  }

}


function 게임시작전(){
  if(게임진행 == false){
    ctx.clearRect(0,0,grid.width, grid.height);
    //cancelAnimationFrame(animation);
    clearInterval(interval);
    게임시작.draw();
    player.draw();
    
  }
}


function init(){ // 캔버스 초기화

  //document.body.appendChild(canvas); //맨뒤
   
  //document.body.prepend(canvas); //맨앞
  
  
  let canvasfield = document.querySelector('.canvasfield');
  
  canvasfield.insertBefore(canvas, canvasfield.canvas);

  canvas.width = grid.width;

  canvas.height = grid.height;

  canvas.style.border = '1px solid black';

  //player.x = 350;

  //player.y = 395;
 
  rock.count = 8;

  for(var i =0 ; i < rock.count ; i++)

  rock[i] = {};



}



init();


var interval = setInterval(실행,10);
    
function 실행(){  //일정한 시간 간격으로 작업을 수행
  
  draw();
  게임시작전();
  updatePosition();
  max_score.addEventListener("input", tempFunc);
   

   //console.log('Players Pos : (' + player.x +','+ player.y +')');

   //console.log(frame);

   frame+=1;

  if(frame%10 ==0)

  score +=1;


}



document.addEventListener('keydown', function(e){
    if(e.code === 'Space'){
        e.preventDefault();
        if(게임진행 == false){
            rock = [];
    
            score=0;
    
            key ={};
    
            frame =0;
    
            게임진행 = true;
            init();
            interval = setInterval(실행,10);
        }
    }
  });

window.addEventListener("keydown", function(e){

  key[e.keyCode] = true;

});

window.addEventListener("keyup", function(e){

  key[e.keyCode] = false;

});

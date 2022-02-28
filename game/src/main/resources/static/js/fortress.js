const canvas = document.getElementById('canvas');
const ctx = canvas.getContext('2d');

canvas.width = 1000;
canvas.height = 500;
const width = canvas.width;
const height = canvas.height;

let 게임진행 = false;
let score = document.getElementById('now_score');
let max_score = document.getElementById("max_score");
let guest = document.getElementById("guest");
let level = 1;
let life = 3;
let timer = 0;
let 구름속도 = 4;
let 구름설치 = 180;
let cloud여러개 = [];
let bottomY = 440;



let 레벨 = {
    x: 870,
    y: 20,
    draw() {
        ctx.font = "17pt Fira";
        ctx.fillStyle = 'black';
        ctx.fillText("Level : " + level, this.x, this.y);
    }
}
let 목숨 = {
    x: 875,
    y: 55,
    draw() {
        ctx.font = "17pt Fira";
        ctx.fillStyle = 'black';
        ctx.fillText(" ♥   : " + life, this.x, this.y);
    }
}
let 게임시작 = {
    x: 350,
    y: 150,
    draw() {
        ctx.font = "17pt Fira";
        ctx.fillStyle = 'black';
        ctx.fillText("스페이스바를 누르면 게임시작", this.x, this.y);
    }
}

let 게임종료 = {
    x: 500,
    y: 150,
    draw() {
        ctx.font = "17pt Fira";
        ctx.fillStyle = 'black';
        ctx.fillText("게임종료", this.x, this.y);
    }
}

let 재시작 = {
    x: 420,
    y: 180,
    draw() {
        ctx.font = "14pt Fira";
        ctx.fillStyle = 'black';
        ctx.fillText("스페이스바를 누르면 재시작", this.x, this.y);
    }
}

let tankLeftPressed = false; //왼쪽 방향키 입력 여부
let tankRightPressed = false; //오른쪽 방향키 입력 여부
let isCharging = false; //파워게이지 채우는 중인지 여부
let isFired = false; //공이 발사되었는지 여부
let isHitted = false; //공이 목표물에 명중했는지 여부

var img_tank = new Image();
var img_target1 = new Image();
var img_target2 = new Image();
var img_airplane = new Image();
var img_bottom = new Image();
img_tank.src = '../image/tank1.png';
img_target1.src = '../image/target1.png';
img_target2.src = '../image/target2.png';
img_airplane.src = '../image/airplane.png';
img_bottom.src = '../image/bottom.png';

img_airplane.onload = function() {
    tank.draw();
    target.draw();
    ctx.drawImage(img_bottom, 0, bottomY, 1000, 60);
}


let tank = {
    width: 50,
    height: 50,
    x: 50,
    y: 378,
    dx: 5, //x축 이동 속도(이동값)
    dy: 3, //y축 이동 속도(이동값)
    barrelAngle: Math.PI / 4, //포신의 각도(Math.PI는 각도로 환산하면 180도->Math.PI / 4는 45도)
    barrelAngleDIF: Math.PI / 60,  //포신 각도 변화량
    gauge: Math.PI, //파워게이지바
    gaugeBarRadius: 30, //파워게이지바의 반지름
    gaugeDIF: Math.PI / 60, //파워게이지가 충전되는 속도(변화량)
    GRAVITY_ACCELERATION: 0.098, //공이 아래쪽을 받는 힘
    missileRadius: 5, //포의 반지름
    missileX: 0, //포의 x좌표
    missileY: 0, //포의 x좌표
    missileDx: 0, //포의 x축 이동량(이동속도)
    missileDy: 0, //포의 y축 이동량(이동속도)

    draw() {
        //        ctx.fillStyle = 'green';
        //        ctx.fillRect(this.x, this.y, this.width, this.height);
        ctx.drawImage(img_tank, this.x, this.y);

        //포신 그리기
        let centerx = this.x + (this.width / 2); //포신 그리기 위한 원점의 x좌표(탱크 중앙x좌표)
        let centery = this.y + (this.height / 2); //포신 그리기 위한 원점의 y좌표(탱크 중앙y좌표)
        let barrelLength = this.width * Math.sqrt(1); //포신의 길이

        ctx.beginPath();
        ctx.moveTo(centerx, centery);
        ctx.lineTo(
            centerx + barrelLength * Math.cos(this.barrelAngle),// 탱크 중앙x좌표 + 포신의 각도에 따른 삼각형 밑변의 길이
            centery - barrelLength * Math.sin(this.barrelAngle)// 탱크 중앙y좌표 + 포신의 각도에 따른 삼각형 대변의 길이
        );
        ctx.stroke();
        ctx.closePath();

        //파워게이지 그리기 
        ctx.beginPath();
        ctx.arc( //arc는 호, 원을 그리는 메소드, arc(원점좌표, 반지름길이, 시작각도, 끝각도, 방향(defualt:시계방향))
            centerx,
            centery - barrelLength,
            this.gaugeBarRadius,
            Math.PI,
            this.gauge,
            false
        );
        ctx.stroke();

        //포 그리기
        if (!isFired) { //발사 안하면 포는 포신 끝에 위치함
            this.missileX = centerx + barrelLength * Math.cos(this.barrelAngle)//포신 끝부분 x좌표
            this.missileY = centery - barrelLength * Math.sin(this.barrelAngle);//포신 끝부분 y좌표

        } else { //발사할 경우 위치 이동
            this.missileDy -= this.GRAVITY_ACCELERATION; //중력의 영향으로 y좌표 감소
            this.missileX = this.missileX + this.missileDx;
            this.missileY = this.missileY - this.missileDy;
        }

        ctx.beginPath();
        ctx.arc(this.missileX, this.missileY, this.missileRadius, 0, Math.PI * 2);
        ctx.fillStyle = "blue";
        ctx.fill();
        ctx.closePath();
    }
};

class Target {
    constructor(x, y) {
        this.width = Math.floor(Math.random() * y + x);
        this.height = Math.floor(Math.random() * y + x);
        this.x = Math.floor(Math.random() * (500 - this.width) + 500);
        this.y = height - this.height - 50;
    }
    draw() {
        //        ctx.fillStyle = 'red';
        //        ctx.fillRect(this.x, this.y, this.width, this.height);
        ctx.drawImage(img_target2, this.x, this.y, this.width, this.height);
    }
};

class Cloud {
    constructor(y) {
        this.x = 1000;
        this.y = y;
        this.width = 140;
        this.height = 70;
    }

    draw() {
        ctx.drawImage(img_airplane, this.x, this.y, this.width, this.height);
    }
}


var target = new Target(50, 10);
function 프레임마다실행() {
    animation = requestAnimationFrame(프레임마다실행);
    ctx.clearRect(0, 0, width, height);

    게임시작전();
    timer++;

    if (timer % 구름설치 === 0) {
        var cloud = new Cloud(random = randomNum(20, 150));
        cloud여러개.push(cloud);
        cloud.draw();
    }


    cloud여러개.forEach((a, i, o) => {
        //x좌표가 0미만이면 제거
        if (a.x + a.width < 0) {
            o.splice(i, 1)
        }
        a.x -= 구름속도;
        a.draw();
    })

    // 방향키 입력 이벤트에 따른 탱크 움직임
    if (tankLeftPressed && tank.x > 0) {
        tank.x -= tank.dx;
    }
    if (tankRightPressed && tank.x + tank.width < width) {
        tank.x += tank.dx;
    }

    //isCharging에 따른 파워게이지 충전
    if (isCharging && !isFired) { //충전중이고 발사 안됐을 때
        if (tank.gauge < Math.PI * 2) {//gauge는 초기값인 Math.PI 에서 시작해서 Math.PI * 2 까지 충전 가능
            tank.gauge += tank.gaugeDIF;
        }
    } else if (isFired) { //발사 됐을 때
        checkMissile();
    }
    ctx.drawImage(img_bottom, 0, bottomY, 1000, 60);//바닥
    tank.draw();
    target.draw();
    레벨.draw();
    목숨.draw();

};

프레임마다실행();

const keydownHandler = event => {
    if (게임진행 == true) {

        if (event.keyCode === 37) { //왼쪽 방향키
            event.preventDefault();
            tankLeftPressed = true;
        } else if (event.keyCode === 39) { //오른쪽 방향키
            event.preventDefault();
            tankRightPressed = true;
        } else if (event.keyCode === 38) { //위쪽 방향키(포신 각도 조절)
            event.preventDefault();
            if (tank.barrelAngle < Math.PI) {
                tank.barrelAngle += tank.barrelAngleDIF;
            }
        } else if (event.keyCode === 40) { //아래쪽 방향키(포신 각도 조절)
            event.preventDefault();
            if (tank.barrelAngle > 0) {
                tank.barrelAngle -= tank.barrelAngleDIF;
            }
        } else if (event.keyCode === 32 && !isFired) { //스페이스바(파워게이지 충전)
            event.preventDefault();
            isCharging = true;
        } else if (event.keyCode === 32) {
            event.preventDefault();
        }
    }
    else {
        if (event.keyCode === 32) {
            event.preventDefault();
        }
    }
};

const keyupHandler = event => {
    if (게임진행 == true) {
        if (event.keyCode === 37) { //왼쪽 방향키
            tankLeftPressed = false;
        } else if (event.keyCode === 39) {
            tankRightPressed = false; //오른쪽 방향키
        } else if (event.keyCode === 32 && !isFired) { //스페이스바(포 발사)
            isCharging = false;
            isFired = true;
            missilePower = tank.gauge * 1.6;
            tank.missileDx = missilePower * Math.cos(tank.barrelAngle);
            tank.missileDy = missilePower * Math.sin(tank.barrelAngle);
            tank.gauge = Math.PI;
        }
    } else {
        if (event.keyCode === 32 && !isFired) { //스페이스바(게임 시작)
            게임진행 = true;
            level = 1;
            life = 3;
            isHitted = false;
            isCharging = false; //파워게이지 채우는 중인지 여부
            isFired = false; //공이 발사되었는지 여부
            프레임마다실행();
        }
    }
};

document.addEventListener("keydown", keydownHandler, false);
document.addEventListener("keyup", keyupHandler, false);

function 게임시작전() {
    if (게임진행 == false) {
        ctx.clearRect(0, 0, width, height);
        cancelAnimationFrame(animation);
        게임시작.draw();
        tank.draw();

    }
}

//명중 여부 체크
function checkMissile() {
    // canvas 왼쪽, 오른쪽, 아래 벽에 닿으면
    if (tank.missileX <= 0 || tank.missileX >= width || tank.missileY >= bottomY) {
        isFired = false;
        isHitted = false;
        life--;
        if (life == 0) {
            cancelAnimationFrame(animation);
            게임진행 = false;
            재시작.draw();
            게임종료.draw();
            now_score.value = level;


            // 신기록 갱신
            if (parseInt(now_score.value) > parseInt(max_score.value)) {
                // 현재점수가 최고점수보다 높다면
                max_score.value = now_score.value; // 현재점수를 최고점수에 저장

                if (guest.value == 'false') { // 게스트계정이 아닌경우 최고점수 갱신

                    $.ajax({                 // DB에 최고점수 업데이트
                        url: '/game/fortress',
                        type: 'post',
                        data: { score: parseInt(max_score.value) },
                        success: function(data) {
                            console.log("1 = 성공 / 0 = 실패 : " + data);

                        },
                        error: function() {
                            console.log("실패");
                        }
                    });
                }
            }
        }
    }

    // target 명중
    if (
        tank.missileX >= target.x &&
        tank.missileX <= target.x + target.width &&
        tank.missileY >= target.y
    ) {
        isFired = false;
        isHitted = true;
        level++;
        life = 3;
        cancelAnimationFrame(animation);
        drawTarget(level);
        프레임마다실행();
    }
    //비행기 충돌
    if (
        tank.missileX >= cloud.x &&
        tank.missileX <= cloud.x + target.width &&
        tank.missileY >= cloud.y
    ) {
        isFired = false;
        isHitted = true;
        level++;
        life = 3;
        cancelAnimationFrame(animation);
        drawTarget(level);
        프레임마다실행();
    }
};

function drawTarget(level) {
    let x;
    let y;
    if (level < 5) {
        x = 40;
        y = 50;
    } else if (level >= 5 && level < 10) {
        x = 30;
        y = 40;
    } else if (level >= 10 && level < 15) {
        x = 20;
        y = 30;
    } else {
        x = 10;
        y = 20;
    }
    target = new Target(x, y);
};

function randomNum(min, max) {
    var randNum = Math.floor(Math.random() * (max - min + 1)) + min;
    return randNum;
}




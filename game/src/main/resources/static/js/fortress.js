const canvas = document.getElementById('canvas');
const ctx = canvas.getContext('2d');

canvas.width = 1000;
canvas.height = 500;
const width = canvas.width;
const height = canvas.height;

let 점프중 = false;
let 게임진행 = false;
let score = document.getElementById('now_score');
let level = 1;
let life = 3;



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

let tank = {
    width: 50,
    height: 50,
    x: 50,
    y: 450,
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
        ctx.fillStyle = 'green';
        ctx.fillRect(this.x, this.y, this.width, this.height);
        //ctx.drawImage(img1, this.x - 5, this.y + 4);

        //포신 그리기
        let centerx = this.x + (this.width / 2); //포신 그리기 위한 원점의 x좌표(탱크 중앙x좌표)
        let centery = this.y + (this.height / 2); //포신 그리기 위한 원점의 y좌표(탱크 중앙y좌표)
        let barrelLength = this.width * Math.sqrt(2); //포신의 길이

        ctx.beginPath();
        ctx.moveTo(centerx, centery);
        ctx.lineTo(
            centerx + barrelLength * Math.cos(this.barrelAngle),// 탱크 중앙x좌표 + 포신의 각도에 따른 삼각형 밑변의 길이
            centery - barrelLength * Math.sin(this.barrelAngle)// 탱크 중앙y좌표 + 포신의 각도에 따른 삼각형 대변의 길이
        );
        ctx.stroke();
        ctx.closePath();
        //        console.log(this.barrelAngle);

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
    constructor() {
        this.width = Math.floor(Math.random() * 100 + 30);
        this.height = Math.floor(Math.random() * 100 + 10);
        this.x = Math.floor(Math.random() * (500 - this.width) + 500);
        this.y = height - this.height;
    }
    draw() {
        ctx.fillStyle = 'red';
        ctx.fillRect(this.x, this.y, this.width, this.height);
        //    ctx.drawImage(cac, this.x,this.y);
    }
};


let target = new Target();
function 프레임마다실행() {
    animation = requestAnimationFrame(프레임마다실행);
    ctx.clearRect(0, 0, width, height);

    게임시작전();
    //    timer++;

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

//    if (!isHitted) {
        tank.draw();
        target.draw();
//    }
    레벨.draw();
    목숨.draw();

    //    drawTarget();
    //    drawMissile();
};

프레임마다실행();

const keydownHandler = event => {
    if (게임진행 == true) {
        if (event.keyCode === 37) { //왼쪽 방향키
            tankLeftPressed = true;
        } else if (event.keyCode === 39) { //오른쪽 방향키
            tankRightPressed = true;
        } else if (event.keyCode === 38 && tank.barrelAngle < Math.PI) { //위쪽 방향키(포신 각도 조절)
            tank.barrelAngle += tank.barrelAngleDIF;
        } else if (event.keyCode === 40 && tank.barrelAngle > 0) { //아래쪽 방향키(포신 각도 조절)
            tank.barrelAngle -= tank.barrelAngleDIF;
        } else if (event.keyCode === 32 && !isFired) { //스페이스바(파워게이지 충전)
            event.preventDefault();
            isCharging = true;
        }
    } else {
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
        } else if (event.keyCode === 32 && !isFired && isHitted){ //레빌업 재시작
//            score.value = 0;
            level++;
            life = 3;
            isHitted = false;
            isCharging = false; //파워게이지 채우는 중인지 여부
            isFired = false; //공이 발사되었는지 여부
            alert("레벨업");
            프레임마다실행();
        }
    } else {
        if (event.keyCode === 32 && !isFired) { //스페이스바(게임 시작)
            게임진행 = true;
//            score.value = 0;
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
    if (tank.missileX <= 0 || tank.missileX >= width || tank.missileY >= height) {
        isFired = false;
        isHitted = false;
        life--;
        if (life == 0) {
            cancelAnimationFrame(animation);
            게임진행 = false;
            재시작.draw();
            게임종료.draw();
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
//        level++;
        cancelAnimationFrame(animation);
        //        게임진행 = false;
                재시작.draw();
        //        게임종료.draw();
//        level++;
//        프레임마다실행();


        //        if (confirm("명중입니다. 다시 하시겠습니까?")) {
        //            location.reload();
        //        } else {
        //            게임종료.draw();
        //        }
    }
};

//function drawTarget(score) {
//    let level = 1;
//    let targetlist = [];
//    if (score > 100) {
//        level += 1;
//    } else if (score > 200) {
//        level += 2;
//    } else if (score > 300) {
//        level += 3;
//    }
//    for (let i = 0; i <= level; i++) {
//        let target = new Target();
//        targetlist.push(target);
//    }
//    return targetlist;
//};




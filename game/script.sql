------------------회원 정보  테이블---------------------------
create table member_table (
mem_id varchar2(50) constraint member_table_mem_id_pk primary key,
mem_pass varchar2(300) constraint member_table_mem_pass_nn not null,
mem_nick varchar2(100) constraint member_table_mem_nick_nn not null
                      constraint member_table_mem_nick_uq unique,                       
mem_name varchar2(100) constraint member_table_mem_name_nn not null,
mem_gender varchar2(5) constraint member_table_mem_gender_nn not null,
mem_tel varchar2(5) constraint member_table_mem_tel_nn not null,
mem_hp varchar2(100) constraint member_table_mem_hp_nn not null
                 constraint member_table_mem_hp_uq unique,
mem_email varchar2(200) constraint member_table_mem_e_nn not null
                 constraint member_table_mem_e_uq unique,     
mem_block varchar2(5) constraint member_table_mem_bl_nn not null,   
mem_date date constraint member_table_mem_d_nn not null,
mem_drop varchar2(5) constraint member_table_mem_dr_nn not null,
mem_block_date date,
mem_block_end_date date,
mem_role varchar2(20)
);




------------------게시판 정보 테이블---------------------------
create table board_table (
board_num varchar2(10) constraint board_table_num_pk primary key,
board_type number(2) constraint board_table_type_nn not null,
board_title varchar2(100) constraint board_table_title_nn not null,
board_content varchar2(4000) constraint board_table_content_nn not null,
mem_id varchar2(50) constraint board_table_id_fk references member_table(mem_id),
board_date date constraint board_table_date_nn not null,
board_update_id varchar2(20),
board_update_date date,
board_hit number(10), 
board_delete varchar2(2) constraint board_table_del_nn not null
);  
-------------------게시판 시퀀스-----------------
create sequence sequ_board_num
start with 1 
increment by 1;

----------------------------------------댓글 테이블-------------------
create table comment_table (
comm_num varchar2(10) constraint comment_table_num_pk primary key,
board_num varchar2(10) constraint comment_table_num_fk references board_table(board_num),
mem_id varchar2(50) constraint comment_table_id_fk references member_table(mem_id),
comm_contents varchar2(400) constraint comment_table_con_nn not null,
comm_update_date date,
comm_update_id varchar2(20),
comm_delete varchar2(2) constraint comment_table_del_nn not null,
comm_date date constraint comment_table_date_nn not null
);
-----------------------------------댓글 시퀀스---------------------------------
create sequence sequ_comment_num
start with 1 
increment by 1;


-----------------------------------신고 정보 테이블---------------------------
create table report_table (
rep_num varchar2(20) constraint report_table_num_pk primary key,
mem_id varchar2(50) constraint report_table_id_fk references member_table(mem_id),
rep_id varchar2(20) constraint report_table_rep_id_nn not null,
rep_type varchar2(10) constraint report_table_type_nn not null,
board_num varchar2(10) constraint report_table_num_fk references board_table(board_num),
comm_num varchar2(10) constraint report_table_comm_num_fk references comment_table(comm_num),
rep_date date
);


---------------------------신고 시퀀스----------------------
create sequence sequ_report_num
start with 1 
increment by 1;

--------------------------------게임 정보 테이블------------------
create table game_table (
game_name varchar2(100) constraint game_table_name_pk primary key,
game_rule varchar2(2000),
game_explan varchar2(4000),
game_maker varchar2(50),
game_create_date date,
game_modifier varchar2(50),
game_update_date date
);
---------------------------------게임 시퀀스------------------------------
create sequence sequ_game_name
start with 1 
increment by 1;


---------------------------랭크 테이블---------------------------
create table rank_table (
rank_score number(15) constraint rank_table_score_nn not null,
mem_id varchar2(50) constraint rank_table_id_fk references member_table(mem_id),
game_name varchar2(50) constraint rank_table_name_fk references game_table(game_name),
constraint rank_table_pk primary key (mem_id, game_name)
);

---------------------------게임 이름 삽입--------------------
insert into game_table values ('똥피하기', '장애물을 스페이스 바로 피함', '공룡피하기 게임입니다', '재밌조', sysdate, '수정자',sysdate); 
insert into game_table values ('포트리스', '장애물을 스페이스 바로 피함', '공룡피하기 게임입니다', '재밌조', sysdate, '수정자',sysdate); 
insert into game_table values ('2048', '장애물을 스페이스 바로 피함', '공룡피하기 게임입니다', '재밌조', sysdate, '수정자',sysdate); 
insert into game_table values ('같은그림찾기', '장애물을 스페이스 바로 피함', '공룡피하기 게임입니다', '재밌조', sysdate, '수정자',sysdate); 
insert into game_table values ('공룡게임', '장애물을 스페이스 바로 피함', '공룡피하기 게임입니다', '재밌조', sysdate, '수정자',sysdate); 


-----------------------------관리자, 유저 Id 만들기------------------
--관리자용 쿼리
update member_table set mem_id='admin', mem_nick='관리자1', mem_role='admin' where mem_id='회원가입시 아이디';

--유저용 쿼리
update member_table set mem_id='user', mem_nick='유저1', mem_role='user' where mem_id='회원가입시 아이디';
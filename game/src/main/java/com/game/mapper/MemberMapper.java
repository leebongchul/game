package com.game.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.game.domain.MemberDTO;

@Mapper
public interface MemberMapper {

	public int insertMember(MemberDTO params);

	public MemberDTO selectMember(MemberDTO params);

	public String selectMemberId(MemberDTO params);

	public int selectMemberPass(MemberDTO params);

	public int updateMemberPass(MemberDTO params);

	public int selectMemberNumber(MemberDTO params);

	public int updateMemberBlock(MemberDTO params);

	public int memberIdcheck(String memId);

	public int memberNickcheck(String memNick);

	public int memberHpcheck(String memHp);

	public int memberEmailcheck(String memEmail);
	
	public int dropMember(MemberDTO params);
	
	public int userUpdate(MemberDTO params);
	

	/** 차단된 유저가 존재하면 '차단'이 출력 됨*/
	public String seeBlockLogin(MemberDTO params);
	
	/**  차단해제*/
	public int clearBlockMember(MemberDTO params);

}

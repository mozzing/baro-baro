package com.barobaro.app.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.barobaro.app.vo.PostFileVO;
import com.barobaro.app.vo.PostVO;
import com.barobaro.app.vo.RentTimeSlotVO;

@Repository
@Mapper
public interface PostMapper {
	int insertPostByPostVO(PostVO postVO);
	int insertRentTimeSlotByRentTimeSlotVO(RentTimeSlotVO rentTimeSlotVO);
	int insertPostFileByPostFileVO(PostFileVO postFileVO);
	PostVO selectPostByPostSeq(@Param("postSeq")long postSeq);
}

package com.itwillbs.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.dto.ChatListDTO;
import com.itwillbs.dto.ChatMessageDTO;
import com.itwillbs.dto.ChatRoomHeaderDTO;




@Mapper
public interface ChatMapper {

	List<ChatListDTO> selectMyChatList(
		    @Param("userId") Long userId,
		    @Param("unreadOnly") Boolean unreadOnly
		);
	//채팅방 상대방 정보 가져오기
	 ChatRoomHeaderDTO selectChatRoomHeader(
	            @Param("roomId") Long roomId,
	            @Param("loginUserId") Long loginUserId
	    );
	 //채팅방안 메세지들 가져오기
	  List<ChatMessageDTO> selectMessagesByRoomId(
	            @Param("roomId") Long roomId
	    );
	  int markMessagesAsRead(
		        @Param("roomId") Long roomId,
		        @Param("loginUserId") Long loginUserId
		    );
	
}

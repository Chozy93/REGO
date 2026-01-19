package com.itwillbs.view.condition;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class ChatMessageSendConditionVO {

    private final String content;

    @JsonCreator
    public ChatMessageSendConditionVO(
            @JsonProperty("content") String content
    ) {
        this.content = content;
    }
}

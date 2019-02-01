package com.kakaoix.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TodoDto{
    private Long rnum;
    private Long id; // id
    private boolean status; // 상태 true : 완료, false : 비완료
    @NotNull
    @NotEmpty(message = "Todo 내용을 입력하세요.")
    @Size(max = 255, message = "Todo 내용은 최대 255자 입니다.")
    private String task; // todo contents
    private Long refId; // 참조 ID
    private int refCnt; // 참조 카운터
    private LocalDateTime modDate;
    private LocalDateTime regDate;
    private List<Long> idList;            // 참조가능한 & 완료 체크 id List
    private List<Long> refIdList;       // 참조된 id 리스트\
    private List<TodoDto> refList;        // 참조 todo List
    private List<TodoDto> possRefList;   // 참조 가능한 todo List
}

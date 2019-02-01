package com.kakaoix.dto;

import com.kakaoix.common.PageConfig;
import lombok.Data;

import java.util.List;

@Data
public class PageDto {
    private PageConfig pageConfig;
    private List<TodoDto> todoList;
}

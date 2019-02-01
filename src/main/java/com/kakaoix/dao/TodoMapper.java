package com.kakaoix.dao;
import com.kakaoix.dto.PageDto;
import com.kakaoix.dto.TodoDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TodoMapper {
    public List<TodoDto> selectTodoList(PageDto pageDto);
    public int selectTodoTotCnt();
    public TodoDto selectTodo(Long id);
    public List<TodoDto> selectRefList(Long id);
    public List<TodoDto> selectPossRefList(Long id);
    public void insertTodo(TodoDto todoDto);
    public void updateTodo(TodoDto todoDto);
    public void updateResetRef(TodoDto todoDto);
    public void updateRefTodo(TodoDto todoDto);
    public void deleteTodo(Long id);
    public void updateResetStatus(Long id);

}


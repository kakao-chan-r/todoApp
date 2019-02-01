package com.kakaoix.dao;
import com.kakaoix.dto.PageDto;
import com.kakaoix.dto.TodoDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 @Mapper가 붙은 인터페이스는 따로 구현 클래스를 만들지 않아도 알아서 Bean으로 만들어준다.
 따라서 간단히 위와 같이 선언만 하면 그 내부 구현은 mybatis가 알아서 한다.


 Spring - Mybatis 에서 SQL을 실행 하려면 크게 다음 2가지 방법을 사용 가능하다.

 1) Mapper interface 를 주입받는 방법
 2) SqlSession 을 주입받는 방법
 */
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


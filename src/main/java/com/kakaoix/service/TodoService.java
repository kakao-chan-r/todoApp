package com.kakaoix.service;

import com.kakaoix.common.PageConfig;
import com.kakaoix.dao.TodoMapper;
import com.kakaoix.dto.PageDto;
import com.kakaoix.dto.TodoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TodoService {
    @Resource
    TodoMapper todoMapper;

    @Transactional(readOnly = true)
    public Object getTodoList(PageConfig pageConfig){
        PageDto pageDto = new PageDto();
        pageDto.setPageConfig(pageConfig);
        List<TodoDto> returnDto = todoMapper.selectTodoList(pageDto);
        for ( int i = 0; i < returnDto.size(); i++){
            if(returnDto.get(i).getRefCnt() > 0){ // 참조된 Todo가 존재
                returnDto.get(i).setRefList(todoMapper.selectRefList(returnDto.get(i).getId())); // 참조된 id List set(상태정보 포함)
            }
        }
        pageDto.setTodoList(returnDto);
        return pageDto;
    }

    @Transactional(readOnly = true)
    public int selectTodoTotCnt(){
        return todoMapper.selectTodoTotCnt();
    }

    @Transactional(readOnly = true)
    public TodoDto readTodo(Long id){
        TodoDto todoDtlDto = todoMapper.selectTodo(id);                // 자신의 todo 상세 정보
        todoDtlDto.setRefList(todoMapper.selectRefList(id));           // 참조된 todo list
        todoDtlDto.setPossRefList(todoMapper.selectPossRefList(id));   // 참조가능한 todo list
        return todoDtlDto;
    }

    @Transactional
    public void addTodo(TodoDto todoDto){
        todoMapper.insertTodo(todoDto);
    }

    @Transactional
    public void modifyTodo(TodoDto todoDto) throws Exception {
        List<Long> refIdList = todoDto.getRefIdList();
        // 완료 상태로 변경 시 참조된 todo 중 미완료 리스트 존재하지 않는 경우만 Update
        if(todoDto.isStatus() == true && refIdList != null && refIdList.size() > 0){
            for (Long id : refIdList) {
                TodoDto dto = todoMapper.selectTodo(id);
                if(dto.isStatus() == false){ // 자신을 참조하는 todo 중 미완료 존재
                    throw new RuntimeException("참조된 Todo 중 완료되지 않은 항목이 있습니다.");
                }
            }
        }
        // 완료 > 미완료 상태로 변경 시 자신을 참조하고 있는 todo 상태 변경(false)
        if(todoDto.isStatus() == false && todoMapper.selectTodo(todoDto.getId()).isStatus() == true){
            todoMapper.updateResetStatus(todoDto.getId());
        }
        if(todoDto.getIdList().size() > 0)    todoMapper.updateResetRef(todoDto); // 수정대상의 참조관계 초기화
        if(todoDto.getRefIdList().size() > 0) todoMapper.updateRefTodo(todoDto);  // 수정대상의 참조관계 update
        todoMapper.updateTodo(todoDto);// 본문 update
    }

    @Transactional
    public void deleteTodo(Long id) throws Exception {
        List<TodoDto> todoDtoList = todoMapper.selectRefList(id);
        deleteTodoUpdate(todoDtoList);
        todoMapper.deleteTodo(id); // 본문 delete
    }

    @Transactional
    public void deleteAllTodo(TodoDto todoDto) throws Exception {
        List<Long> idList = todoDto.getIdList();
        if(idList.size() > 0) {
            for(int i = 0; i<idList.size(); i++){
                List<TodoDto> todoDtoList = todoMapper.selectRefList(idList.get(i));
                deleteTodoUpdate(todoDtoList);
                todoMapper.deleteTodo(idList.get(i)); // 본문 delete
            }
        }
    }

    @Transactional
    public void deleteTodoUpdate(List<TodoDto> todoDtoList){
        if(todoDtoList.size() > 0) { //삭제대상 todo에서 참조된 항목이 있을 경우 참조관계 초기화
            List idList = new ArrayList(); //id를 담을 리스트
            for (TodoDto dto : todoDtoList) {
                idList.add(dto.getId());
            }
            TodoDto todoDto = new TodoDto();
            todoDto.setIdList(idList);
            todoMapper.updateResetRef(todoDto);
        }
    }
}

package com.kakaoix.controller;

import com.kakaoix.common.PageConfig;
import com.kakaoix.dto.TodoDto;
import com.kakaoix.service.TodoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@Slf4j
/* Lombok 로그용 */
public class TodoRestApiController {
    private TodoService todoService;

    public TodoRestApiController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/todoList")
    public ResponseEntity<?> getTodoList (
            final TodoDto todoDto,
            @RequestParam(value = "currentPage") Optional<Integer> currentPage,
            @RequestParam(value = "perPageCnt") Optional<Integer> perPageNum,
            @RequestParam(value = "displayPageNum") Optional<Integer> displayPageNum) throws Exception {
        PageConfig pageConfig = new PageConfig(currentPage.orElse(1),perPageNum.orElse(10),displayPageNum.orElse(3),todoService.selectTodoTotCnt());
        return ResponseEntity.ok(todoService.getTodoList(pageConfig));
    }

    @PostMapping("/addTodo")
    public ResponseEntity<?> addTodo(@RequestBody @Valid final TodoDto todoDto, BindingResult bindingResult) throws Exception {
        for (FieldError error : bindingResult.getFieldErrors()){
            return new ResponseEntity<>(error.getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }
        todoService.addTodo(todoDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/readTodo/{id}")
    public ResponseEntity<?> getTodoDtl(@PathVariable final Long id) throws Exception {
        return ResponseEntity.ok(todoService.readTodo(id));
    }

    @PutMapping("/modifyTodo")
    public ResponseEntity<?> modifyTodo(@RequestBody @Valid final TodoDto todoDto, BindingResult bindingResult) throws Exception {
        for (FieldError error : bindingResult.getFieldErrors()){
            return new ResponseEntity<>(error.getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }
        todoService.modifyTodo(todoDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/deleteTodo/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable  final Long id) throws Exception {
        todoService.deleteTodo(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<?> checkedTodoDelete(@RequestBody final TodoDto todoDto) throws Exception {
        todoService.deleteAllTodo(todoDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

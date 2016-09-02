package com.example.e4.rcp.todo.model;

import java.util.List;

public interface ITodoService {
	Todo getTodo(long id);

	boolean saveTodo(Todo todo);

	boolean deleteTodo(long id);

	List<Todo> getTodos();

}

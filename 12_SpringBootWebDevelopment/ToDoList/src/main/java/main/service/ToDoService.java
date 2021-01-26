package main.service;

import java.util.List;
import main.model.ToDo;

public interface ToDoService {

  int add(ToDo toDo);

  ToDo get(int id);

  List<ToDo> list();

  ToDo doComplete(int id);

  void remove(int id);

  void removeAll();

}

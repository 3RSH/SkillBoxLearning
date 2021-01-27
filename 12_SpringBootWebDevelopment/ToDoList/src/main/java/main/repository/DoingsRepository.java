package main.repository;

import java.util.List;
import main.model.ToDo;

public interface DoingsRepository {

  int addToDo(ToDo toDo);

  ToDo getToDo(int toDoId);

  List<ToDo> getAllDoings();

  ToDo completeToDo(int toDoId);

  void removeToDo(int toDoId);

  void clearDoings();

  List<ToDo> searchByName(String name);
}

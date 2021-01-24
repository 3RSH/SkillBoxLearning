package main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import response.ToDo;

public class Storage {

  private static int currentId = 1;
  private static ConcurrentHashMap<Integer, ToDo> doings = new ConcurrentHashMap<Integer, ToDo>();

  public static int addToDo(ToDo toDo) {
    int id = currentId++;
    toDo.setId(id);
    toDo.setComplete(false);
    doings.put(id, toDo);
    return id;
  }

  public static ToDo getToDo(int toDoId) {
    if (doings.containsKey(toDoId)) {
      return doings.get(toDoId);
    }
    return null;
  }

  public static List<ToDo> getAllDoings() {
    ArrayList<ToDo> toDoList = new ArrayList<>();
    toDoList.addAll(doings.values());
    return toDoList;
  }

  public static ToDo completeToDo(int toDoId) {
    if (doings.containsKey(toDoId)) {
      doings.get(toDoId).setComplete(true);
      return doings.get(toDoId);
    }
    return null;
  }

  public static void removeToDo(int toDoId) {
    doings.remove(toDoId);
  }

  public static void clearDoings() {
    doings.clear();
    currentId = 1;
  }
}

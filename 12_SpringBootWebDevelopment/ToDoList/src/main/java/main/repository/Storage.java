package main.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import main.model.ToDo;
import org.springframework.stereotype.Service;

@Service
public class Storage implements DoingsRepository {

  private static int currentId = 1;
  private static ConcurrentHashMap<Integer, ToDo> doings = new ConcurrentHashMap<Integer, ToDo>();

  @Override
  public int addToDo(ToDo toDo) {
    int id = currentId++;
    toDo.setId(id);
    toDo.setComplete(false);
    doings.put(id, toDo);
    return id;
  }

  @Override
  public ToDo getToDo(int toDoId) {
    if (doings.containsKey(toDoId)) {
      return doings.get(toDoId);
    }
    return null;
  }

  @Override
  public List<ToDo> getAllDoings() {
    ArrayList<ToDo> toDoList = new ArrayList<>();
    toDoList.addAll(doings.values());
    return toDoList;
  }

  @Override
  public ToDo completeToDo(int toDoId) {
    if (doings.containsKey(toDoId)) {
      doings.get(toDoId).setComplete(true);
      return doings.get(toDoId);
    }
    return null;
  }

  @Override
  public void removeToDo(int toDoId) {
    doings.remove(toDoId);
  }

  @Override
  public void clearDoings() {
    doings.clear();
    currentId = 1;
  }

  @Override
  public List<ToDo> searchByName(String name) {
    List<ToDo> resultDoings = new ArrayList<>();

    for (ToDo toDo : this.doings.values()) {
      if (toDo.getName().contains(name)) {
        resultDoings.add(toDo);
      }
    }

    return resultDoings;
  }
}

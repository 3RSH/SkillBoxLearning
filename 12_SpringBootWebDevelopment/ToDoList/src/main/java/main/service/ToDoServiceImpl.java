package main.service;

import java.util.ArrayList;
import java.util.List;
import main.model.ToDo;
import main.repository.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ToDoServiceImpl implements ToDoService {

  @Autowired
  private Storage doingsRepository;

  @Override
  public int add(ToDo toDo) {
    return doingsRepository.addToDo(toDo);
  }

  @Override
  public ToDo get(int id) {
    return doingsRepository.getToDo(id);
  }

  @Override
  public List<ToDo> list() {
    return doingsRepository.getAllDoings();
  }

  @Override
  public ToDo doComplete(int id) {
    return doingsRepository.completeToDo(id);
  }

  @Override
  public void remove(int id) {
    doingsRepository.removeToDo(id);
  }

  @Override
  public void removeAll() {
    doingsRepository.clearDoings();
  }

  @Override
  public List<ToDo> searchByName(String name) {
    return doingsRepository.searchByName(name);
  }
}

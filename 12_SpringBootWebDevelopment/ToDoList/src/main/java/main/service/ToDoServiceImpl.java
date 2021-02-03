package main.service;

import java.util.List;
import main.model.ToDo;
import main.repository.DoingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ToDoServiceImpl implements ToDoService {

  @Autowired
  @Qualifier("MysqlStorage")
  private final DoingsRepository doingsRepository;

  public ToDoServiceImpl(@Qualifier("MysqlStorage")
      DoingsRepository doingsRepository) {
    this.doingsRepository = doingsRepository;
  }

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

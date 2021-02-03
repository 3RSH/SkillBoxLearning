package main.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import main.model.ToDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("MysqlStorage")
public class MysqlStorage implements DoingsRepository {

  @Autowired
  private final MySqlDoingsRepository sqlRepository;

  public MysqlStorage(MySqlDoingsRepository sqlRepository) {
    this.sqlRepository = sqlRepository;
  }

  @Override
  public int addToDo(ToDo toDo) {
    return sqlRepository.save(toDo).getId();
  }

  @Override
  public ToDo getToDo(int toDoId) {
    Optional<ToDo> toDo = sqlRepository.findById(toDoId);
    return toDo.orElse(null);
  }

  @Override
  public List<ToDo> getAllDoings() {
    List<ToDo> doings = new ArrayList<>();
    sqlRepository.findAll().forEach(doings::add);
    return doings;
  }

  @Override
  public ToDo completeToDo(int toDoId) {
    Optional<ToDo> toDo = sqlRepository.findById(toDoId);

    if (toDo.isPresent()) {
      toDo.get().setComplete(true);
      sqlRepository.save(toDo.get());
    }

    return toDo.orElse(null);
  }

  @Override
  public void removeToDo(int toDoId) {
    sqlRepository.deleteById(toDoId);
  }

  @Override
  public void clearDoings() {
    sqlRepository.deleteAll();
  }

  @Override
  public List<ToDo> searchByName(String name) {
    List<ToDo> doings = new ArrayList<>();
    sqlRepository.findAll().forEach(toDo -> {
      if (toDo.getName().contains(name)) {
        doings.add(toDo);
      }
    });

    return doings;
  }
}

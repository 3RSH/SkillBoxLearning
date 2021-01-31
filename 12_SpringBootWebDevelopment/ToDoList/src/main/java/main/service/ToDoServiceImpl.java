package main.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import main.model.ToDo;
import main.repository.MySqlDoingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ToDoServiceImpl implements ToDoService {

  @Autowired
  private MySqlDoingsRepository doingsRepository;

  @Override
  public int add(ToDo toDo) {
    return doingsRepository.save(toDo).getId();
  }

  @Override
  public ToDo get(int id) {
    Optional<ToDo> toDo = doingsRepository.findById(id);
    return toDo.orElse(null);
  }

  @Override
  public List<ToDo> list() {
    List<ToDo> doings = new ArrayList<>();
    doingsRepository.findAll().forEach(doings::add);
    return doings;
  }

  @Override
  public ToDo doComplete(int id) {
    Optional<ToDo> toDo = doingsRepository.findById(id);

    if (toDo.isPresent()) {
      toDo.get().setComplete(true);
      doingsRepository.save(toDo.get());
    }

    return toDo.orElse(null);
  }

  @Override
  public void remove(int id) {
    doingsRepository.deleteById(id);
  }

  @Override
  public void removeAll() {
    doingsRepository.deleteAll();
  }

  @Override
  public List<ToDo> searchByName(String name) {
    List<ToDo> doings = new ArrayList<>();
    doingsRepository.findAll().forEach(toDo -> {
      if (toDo.getName().contains(name)) {
        doings.add(toDo);
      }
    });

    return doings;
  }
}

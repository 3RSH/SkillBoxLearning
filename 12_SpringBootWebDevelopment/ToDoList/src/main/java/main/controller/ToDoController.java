package main.controller;

import java.util.List;
import main.model.ToDo;
import main.service.ToDoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/doings")
public class ToDoController {

  private final ToDoServiceImpl service;

  @Autowired
  public ToDoController(ToDoServiceImpl service) {
    this.service = service;
  }

  @PostMapping
  public int add(ToDo toDo) {
    return service.add(toDo);
  }

  @GetMapping("/{id}")
  public ResponseEntity get(@PathVariable int id) {
    final ToDo toDo = service.get(id);

    return toDo != null
        ? new ResponseEntity(toDo, HttpStatus.OK)
        : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
  }

  @GetMapping
  public List<ToDo> list() {
    return service.list();
  }

  @PutMapping("/{id}")
  public ResponseEntity doComplete(@PathVariable int id) {
    final ToDo toDo = service.doComplete(id);

    return toDo != null
        ? new ResponseEntity(toDo, HttpStatus.OK)
        : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity remove(@PathVariable int id) {
    service.remove(id);
    return ResponseEntity.status(HttpStatus.OK).body(null);
  }

  @DeleteMapping
  public ResponseEntity removeAll() {
    service.removeAll();
    return ResponseEntity.status(HttpStatus.OK).body(null);
  }

  @GetMapping("/search")
  public ResponseEntity searchByName(String name) {
    List<ToDo> doings = service.searchByName(name);

    return new ResponseEntity(doings, HttpStatus.OK);
  }
}
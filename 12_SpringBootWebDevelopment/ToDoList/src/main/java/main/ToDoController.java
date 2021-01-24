package main;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import response.ToDo;

@RestController
public class ToDoController {

  @PostMapping("/doings/")
  public int add(ToDo toDo) {
    return Storage.addToDo(toDo);
  }

  @GetMapping("/doings/{id}")
  public ResponseEntity get(@PathVariable int id) {
    ToDo toDo = Storage.getToDo(id);
    if (toDo == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    return new ResponseEntity(toDo, HttpStatus.OK);
  }

  @GetMapping("/doings/")
  public List<ToDo> list() {
    return Storage.getAllDoings();
  }

  @PutMapping("/doings/{id}")
  public ResponseEntity doComplete(@PathVariable int id) {
    ToDo toDo = Storage.completeToDo(id);
    if (toDo == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    return new ResponseEntity(toDo, HttpStatus.OK);
  }

  @DeleteMapping("/doings/{id}")
  public ResponseEntity remove(@PathVariable int id) {
    Storage.removeToDo(id);
    return ResponseEntity.status(HttpStatus.OK).body(null);
  }

  @DeleteMapping("/doings/")
  public ResponseEntity removeAll() {
    Storage.clearDoings();
    return ResponseEntity.status(HttpStatus.OK).body(null);
  }
}

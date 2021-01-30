package main.controller;

import java.util.Date;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController {

  @RequestMapping("/")
  public String index() {
    return ("ToDoList project: " + new Date().toString());
  }
}

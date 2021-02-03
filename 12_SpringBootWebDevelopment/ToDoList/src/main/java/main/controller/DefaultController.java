package main.controller;

import java.util.ArrayList;
import main.model.ToDo;
import main.repository.MySqlDoingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DefaultController {

  @Autowired
  MySqlDoingsRepository sqlRepository;

  @RequestMapping("/")
  public String index(Model model) {
    Iterable<ToDo> toDoIterable = sqlRepository.findAll();
    ArrayList<ToDo> doings = new ArrayList<>();
    for (ToDo toDo : toDoIterable) {
      doings.add(toDo);
    }

    model.addAttribute("doings", doings);
    model.addAttribute("count", doings.size());

    return "index";
  }
}

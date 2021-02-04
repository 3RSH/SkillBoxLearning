package main.controller;

import java.util.ArrayList;
import main.model.ToDo;
import main.repository.MySqlDoingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DefaultController {

  @Autowired
  private MySqlDoingsRepository sqlRepository;

  @Value("${version}")
  private String appVersion;

  @RequestMapping("/")
  public String index(Model model) {
    Iterable<ToDo> toDoIterable = sqlRepository.findAll();
    ArrayList<ToDo> doings = new ArrayList<>();
    for (ToDo toDo : toDoIterable) {
      doings.add(toDo);
    }

    model.addAttribute("doings", doings);
    model.addAttribute("count", doings.size());
    model.addAttribute("version", appVersion);

    return "index";
  }
}

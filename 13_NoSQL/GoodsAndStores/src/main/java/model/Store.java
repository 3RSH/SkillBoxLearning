package model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

public class Store {

  @Getter
  @Setter
  String name;

  @Getter
  @Setter
  List<Goods> goods;
}

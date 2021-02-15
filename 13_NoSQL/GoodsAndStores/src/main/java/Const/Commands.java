package Const;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Commands {

  COMMAND_ADD_EXIT("ВЫХОД"),
  COMMAND_ADD_STORE("ДОБАВИТЬ_МАГАЗИН [0-9A-Za-zА-я]+"),
  COMMAND_ADD_GOODS("ДОБАВИТЬ_ТОВАР [A-Za-zА-я]+ [0-9]{1,5}"),
  COMMAND_PUT_GOODS("ВЫСТАВИТЬ_ТОВАР [A-Za-zА-я]+ [0-9A-Za-zА-я]+"),
  COMMAND_STATISTIC("СТАТИСТИКА_ТОВАРОВ");

  @Getter
  private final String com;
}

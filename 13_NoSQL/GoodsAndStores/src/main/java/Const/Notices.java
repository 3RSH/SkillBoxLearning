package Const;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Notices {

  STORE_ADDING("Добавлен магазин \"#NAME\"."),
  GOODS_ADDING("Добавлен товар \"#NAME\"."),
  GOODS_PUTTING("Товар с именем \"#GOODS\" добавлен в магазин \"#STORE\"."),

  ERROR_INPUT("ОШИБКА: Команда введена неверно!"),
  ERROR_STORE_ADDING("ОШИБКА: Магазин с именем \"#NAME\" уже существует!"),
  ERROR_STORE_EXIST("ОШИБКА: Магазина с именем \"#NAME\" не существует!"),
  ERROR_STORE_INPUT("ОШИБКА: Название магазина указано некорректно!"),
  ERROR_GOODS_ADDING("ОШИБКА: Товар с именем \"#NAME\" уже существует!"),
  ERROR_GOODS_EXIST("ОШИБКА: Товара с именем \"#NAME\" не существует!"),
  ERROR_GOODS_PUTTING("ОШИБКА: Товар с именем \"#GOODS\" уже есть в магазине \"#STORE\"!");

  @Getter
  private final String note;
}

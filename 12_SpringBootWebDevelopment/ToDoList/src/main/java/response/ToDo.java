package response;

import lombok.Getter;
import lombok.Setter;

public class ToDo {

  @Getter
  @Setter
  private int id;

  @Getter
  @Setter
  private String name;

  @Getter
  @Setter
  private boolean isComplete;
}

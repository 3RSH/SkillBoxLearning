package main.repository;

import main.model.ToDo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MySqlDoingsRepository extends CrudRepository<ToDo, Integer> {

}

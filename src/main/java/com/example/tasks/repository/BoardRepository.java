package com.example.tasks.repository;

import com.example.tasks.model.BoardModel;
import org.springframework.data.repository.CrudRepository;

public interface BoardRepository extends CrudRepository<BoardModel, Integer> {
    Boolean existsByName(String name);
}

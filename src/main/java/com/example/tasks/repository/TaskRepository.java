package com.example.tasks.repository;

import com.example.tasks.model.TaskModel;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<TaskModel, Integer> {
}

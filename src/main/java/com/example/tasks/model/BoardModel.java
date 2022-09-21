package com.example.tasks.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.config.Task;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "tbl_boards")
@Getter
@NoArgsConstructor
public class BoardModel {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "name")
    private String name;

    @OneToMany(targetEntity = TaskModel.class, mappedBy = "board")
    @JsonManagedReference
    private List<TaskModel> tasks;

    public BoardModel (String name) {
        this.name = name;
        this.tasks = new ArrayList<TaskModel>();
    }

    public BoardModel(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.tasks = new ArrayList<TaskModel>();
    }

    public TaskModel newTask(String name) {
        var task = new TaskModel(name, this);
        tasks.add(task);
        return task;
    }
}

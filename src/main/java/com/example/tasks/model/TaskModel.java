package com.example.tasks.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;



@Entity(name = "tbl_tasks")
@Getter
@NoArgsConstructor
public class TaskModel {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "completed_at")
    private Date completedAt;

    @ManyToOne
    @JoinColumn(name = "board_id", referencedColumnName = "id")
    @JsonBackReference
    private BoardModel board;

    public TaskModel(String name) {
        this.name = name;
        this.createdAt = new Date();
        this.completedAt = null;
    }

    public TaskModel(String name, BoardModel board) {
        this.name = name;
        this.createdAt = new Date();
        this.completedAt = null;
        this.board = board;
    }


    public TaskModel(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.createdAt = new Date();
        this.completedAt = null;
    }


    public void complete(Date completedAt) {
        this.completedAt = completedAt;
    }

}

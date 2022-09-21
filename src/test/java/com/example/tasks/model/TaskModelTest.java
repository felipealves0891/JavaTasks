package com.example.tasks.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class TaskModelTest {

    @Test
    public void complete_completed_assertive() {
        //Arrange
        Date completedAt = new Date();
        TaskModel model = new TaskModel("minha tarefa");

        //Act
        model.complete(completedAt);

        //Assert
        Assertions.assertNotNull(model.getCompletedAt());
        Assertions.assertEquals(completedAt, model.getCompletedAt());
    }

}

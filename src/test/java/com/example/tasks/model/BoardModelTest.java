package com.example.tasks.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BoardModelTest {

    @Test
    public void newTask_create_task() {
        //Arrange
        BoardModel board = new BoardModel("meu quadro");

        //Act
        TaskModel task = board.newTask("minha tarefa");

        //Assert
        Assertions.assertNotNull(task);
        Assertions.assertEquals("minha tarefa", task.getName());
        Assertions.assertNotNull(task.getBoard());
        Assertions.assertEquals(board, task.getBoard());
        Assertions.assertNotNull(board.getTasks());
        Assertions.assertTrue(board.getTasks().contains(task));
    }

}

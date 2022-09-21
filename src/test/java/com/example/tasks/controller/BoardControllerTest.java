package com.example.tasks.controller;

import com.example.tasks.model.BoardModel;
import com.example.tasks.model.TaskModel;
import com.example.tasks.model.dto.CreatingBoardDto;
import com.example.tasks.model.dto.CreatingTaskDto;
import com.example.tasks.model.dto.PayloadDto;
import com.example.tasks.repository.BoardRepository;
import com.example.tasks.repository.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.persistence.Id;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Optional;

@SpringBootTest
public class BoardControllerTest {

    @Spy
    @InjectMocks
    private BoardController controller;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private TaskRepository taskRepository;

    @Test
    public void create_notExists_ok() throws URISyntaxException {
        //Arrange
        Integer id = 1;
        String name = "meu quadro";

        BoardModel model = new BoardModel(id, name);

        CreatingBoardDto dto = new CreatingBoardDto();
        dto.name = name;

        Mockito.when(this.boardRepository.save(ArgumentMatchers.any(BoardModel.class))).thenReturn(model);

        //Act
        ResponseEntity<PayloadDto> responseEntity = this.controller.createBoard(dto);

        //Assert
        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity.getBody());

        PayloadDto payload = responseEntity.getBody();
        BoardModel data = (BoardModel) payload.getData();
        Assertions.assertEquals(name, data.getName());
        Assertions.assertEquals(id, data.getId());
    }

    @Test
    public void create_exists_badRequest() {
        //Arrange
        Integer id = 1;
        String name = "meu quadro";

        CreatingBoardDto dto = new CreatingBoardDto();
        dto.name = name;

        Mockito.when(this.boardRepository.existsByName(name))
                .thenReturn(true);

        //Act
        ResponseEntity<PayloadDto> responseEntity = this.controller.createBoard(dto);

        //Assert
        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity.getBody());

        PayloadDto payload = responseEntity.getBody();
        String data = (String) payload.getData();
        Assertions.assertEquals("Já existe um quadro com este nome", data);
    }

    @Test
    public void addTask_boardExists_ok() {
        //Arrange
        Integer boardId = 53;
        String boardName = "Meu quadro";
        String taskName = "Minha tarefa";

        CreatingTaskDto dto = new CreatingTaskDto();
        dto.boardId = boardId;
        dto.name = taskName;

        BoardModel board = new BoardModel(boardId, boardName);
        Mockito.when(this.boardRepository.findById(boardId))
                .thenReturn(Optional.of(board));

        //Act
        ResponseEntity<PayloadDto> responseEntity = this.controller.addTask(dto);

        //Assert
        Mockito.verify(this.taskRepository).save(ArgumentMatchers.any(TaskModel.class));

        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity.getBody());

        PayloadDto payload = Assertions.assertInstanceOf(PayloadDto.class, responseEntity.getBody());
        TaskModel task = Assertions.assertInstanceOf(TaskModel.class, payload.getData());
        Assertions.assertEquals(board, task.getBoard());
    }

    @Test
    public void addTask_boardNotExists_badRequest() {
        //Arrange
        Integer boardId = 53;
        String taskName = "Minha tarefa";

        CreatingTaskDto dto = new CreatingTaskDto();
        dto.boardId = boardId;
        dto.name = taskName;

        Mockito.when(this.boardRepository.findById(boardId))
                .thenReturn(Optional.empty());

        //Act
        ResponseEntity<PayloadDto> responseEntity = this.controller.addTask(dto);

        //Assert
        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity.getBody());

        PayloadDto payload = Assertions.assertInstanceOf(PayloadDto.class, responseEntity.getBody());
        String text = Assertions.assertInstanceOf(String.class, payload.getData());

        Assertions.assertEquals("Não existe quadro com o Id selecionado!", text);
    }

    @Test
    public void completeTask_exists_ok() {
        //Arrange
        Integer id = 5820;
        String name = "minha tarefa completa";
        TaskModel task = new TaskModel(id, name);

        Mockito.when(this.taskRepository.findById(id))
                .thenReturn(Optional.of(task));

        //Act
        ResponseEntity<PayloadDto> responseEntity = this.controller.completeTask(id);

        //Act
        Mockito.verify(this.taskRepository).save(ArgumentMatchers.eq(task));

        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity.getBody());

        PayloadDto payload = Assertions.assertInstanceOf(PayloadDto.class, responseEntity.getBody());
        TaskModel result = Assertions.assertInstanceOf(TaskModel.class, payload.getData());

        Assertions.assertNotNull(result.getCompletedAt());
    }

    @Test
    public void completeTask_notExists_badRequest() {
        //Arrange
        Integer id = 5820;
        String name = "minha tarefa completa";

        Mockito.when(this.taskRepository.findById(id))
                .thenReturn(Optional.empty());

        //Act
        ResponseEntity<PayloadDto> responseEntity = this.controller.completeTask(id);

        //Assert
        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity.getBody());

        PayloadDto payload = Assertions.assertInstanceOf(PayloadDto.class, responseEntity.getBody());
        String text = Assertions.assertInstanceOf(String.class, payload.getData());

        Assertions.assertEquals("Não existe tarefa com o Id selecionado!", text);
    }

    @Test
    public void completeTask_alreadyComplete_badRequest() {
        //Arrange
        Integer id = 5820;
        String name = "minha tarefa completa";
        TaskModel task = new TaskModel(id, name);
        task.complete(new Date());

        Mockito.when(this.taskRepository.findById(id))
                .thenReturn(Optional.of(task));

        //Act
        ResponseEntity<PayloadDto> responseEntity = this.controller.completeTask(id);

        //Assert
        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity.getBody());

        PayloadDto payload = Assertions.assertInstanceOf(PayloadDto.class, responseEntity.getBody());
        String text = Assertions.assertInstanceOf(String.class, payload.getData());

        Assertions.assertEquals("A tarefa já foi concluida!", text);

    }
}

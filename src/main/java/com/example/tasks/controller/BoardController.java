package com.example.tasks.controller;

import com.example.tasks.model.BoardModel;
import com.example.tasks.model.TaskModel;
import com.example.tasks.model.dto.CreatingBoardDto;
import com.example.tasks.model.dto.CreatingTaskDto;
import com.example.tasks.model.dto.PayloadDto;
import com.example.tasks.repository.BoardRepository;
import com.example.tasks.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/board")
public class BoardController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private BoardRepository boardrepository;

    @PostMapping
    public ResponseEntity<PayloadDto> createBoard(@RequestBody CreatingBoardDto dto) {
        Boolean exists = this.boardrepository.existsByName(dto.name);
        if(exists)
            return factoryResponse401("Já existe um quadro com este nome");

        BoardModel model = new BoardModel(dto.name);
        model = this.boardrepository.save(model);

        PayloadDto payload = new PayloadDto(HttpStatus.CREATED, model);
        return ResponseEntity.created(URI.create("http://localhost/")).body(payload);
    }

    @PostMapping
    @RequestMapping("/task")
    public ResponseEntity<PayloadDto> addTask(@RequestBody CreatingTaskDto dto) {
        Optional<BoardModel> optional = this.boardrepository.findById(dto.boardId);
        if(optional.isEmpty())
            return factoryResponse401("Não existe quadro com o Id selecionado!");

        BoardModel board = optional.get();
        TaskModel task = board.newTask(dto.name);
        this.taskRepository.save(task);
        return factoryResponse200(task);
    }

    @GetMapping
    @RequestMapping("/task/complete/{id}")
    public ResponseEntity<PayloadDto> completeTask(@PathVariable("id") Integer id) {
        Optional<TaskModel> optional = this.taskRepository.findById(id);
        if(optional.isEmpty())
            return factoryResponse401("Não existe tarefa com o Id selecionado!");

        TaskModel task = optional.get();
        if(task.getCompletedAt() != null)
            return factoryResponse401("A tarefa já foi concluida!");

        task.complete(new Date());
        this.taskRepository.save(task);
        return factoryResponse200(task);
    }

    @GetMapping
    public ResponseEntity<PayloadDto> getAll() {
        PayloadDto payload = new PayloadDto(HttpStatus.OK, this.boardrepository.findAll());
        return ResponseEntity.ok().body(payload);
    }

    private ResponseEntity factoryResponse200(Object data) {
        PayloadDto content = factoryPayload(HttpStatus.OK, data);
        return ResponseEntity.ok().body(content);
    }

    private ResponseEntity factoryResponse401(String error) {
        PayloadDto content = factoryPayload(HttpStatus.BAD_REQUEST, error);
        return ResponseEntity.badRequest().body(content);
    }

    private PayloadDto factoryPayload(HttpStatus status, Object data) {
        return new PayloadDto(status, data);
    }

}

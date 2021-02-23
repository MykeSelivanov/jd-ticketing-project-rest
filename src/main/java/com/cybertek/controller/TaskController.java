package com.cybertek.controller;

import com.cybertek.annotation.DefaultExceptionMessage;
import com.cybertek.dto.TaskDTO;
import com.cybertek.entity.ResponseWrapper;
import com.cybertek.exception.TicketingProjectException;
import com.cybertek.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/task")
public class TaskController {

   TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @DefaultExceptionMessage(defaultMessage = "Could not retrieve all tasks!")
    @Operation(summary = "Read all tasks")
    @PreAuthorize("hasAuthority('Manager')")
    public ResponseEntity<ResponseWrapper> readAll(){

        return ResponseEntity.ok(new ResponseWrapper("Successfully retrieved all tasks",taskService.listAllTasks()));
    }

    @GetMapping("/project-manager")
    @DefaultExceptionMessage(defaultMessage = "Could not retrieve all tasks by project manager!")
    @Operation(summary = "Read all tasks by project manager")
    @PreAuthorize("hasAuthority('Manager')")
    public ResponseEntity<ResponseWrapper> readAllByProjectManager() throws TicketingProjectException {
        List<TaskDTO> taskDTOList = taskService.listAllTasksByProjectManager();
        return ResponseEntity.ok(new ResponseWrapper("Successfully retrieved tasks by project manager!",taskDTOList));
    }

    @GetMapping("/{id}")
    @DefaultExceptionMessage(defaultMessage = "Could not retrieve task by Id!")
    @Operation(summary = "Read task by id")
    @PreAuthorize("hasAnyAuthority('Manager','Employee')")
    public ResponseEntity<ResponseWrapper> readById(@PathVariable("id") Long id) throws TicketingProjectException {
        TaskDTO currentTask = taskService.findById(id);
        return ResponseEntity.ok(new ResponseWrapper("Successfully retrieved task by id", currentTask));
    }

    @PostMapping
    @DefaultExceptionMessage(defaultMessage = "Could not create new task!")
    @Operation(summary = "Create new task")
    @PreAuthorize("hasAuthority('Manager')")
    public ResponseEntity<ResponseWrapper> createTask(@RequestBody TaskDTO taskDTO){
        TaskDTO createdTask = taskService.save(taskDTO);
        return ResponseEntity.ok(new ResponseWrapper("Successfully created task", createdTask));
    }

    @DeleteMapping("/{id}")
    @DefaultExceptionMessage(defaultMessage = "Could not delete task!")
    @Operation(summary = "Delete task")
    @PreAuthorize("hasAuthority('Manager')")
    public ResponseEntity<ResponseWrapper> deleteTask(@PathVariable("id") Long id) throws TicketingProjectException {
        taskService.delete(id);
        return ResponseEntity.ok(new ResponseWrapper("Successfully deleted"));
    }

    public ResponseEntity<ResponseWrapper> updateTask





}

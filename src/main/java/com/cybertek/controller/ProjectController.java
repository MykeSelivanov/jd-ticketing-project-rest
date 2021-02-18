package com.cybertek.controller;

import com.cybertek.annotation.DefaultExceptionMessage;
import com.cybertek.dto.ProjectDTO;
import com.cybertek.dto.TaskDTO;
import com.cybertek.dto.UserDTO;
import com.cybertek.entity.ResponseWrapper;
import com.cybertek.enums.Status;
import com.cybertek.exception.TicketingProjectException;
import com.cybertek.service.ProjectService;
import com.cybertek.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/project")
@Tag(name = "Project Controller", description = "Project API")
public class ProjectController {


    private ProjectService projectService;
    private UserService userService;

    @GetMapping
    @DefaultExceptionMessage(defaultMessage = "Could not retrieve all projects!")
    @Operation(summary = "Read all projects")
    @PreAuthorize("hasAnyAuthority('Admin','Manager')")
    public ResponseEntity<ResponseWrapper> readAll(){
       List<ProjectDTO> projectDTOS = projectService.listAllProjects();
       return ResponseEntity.ok(new ResponseWrapper("Projects are retrieved", projectDTOS));
    }

    @GetMapping("/{projectcode}")
    @DefaultExceptionMessage(defaultMessage = "Could not retrieve project by project code!")
    @Operation(summary = "Read by project code")
    @PreAuthorize("hasAnyAuthority('Admin','Manager')")
    public ResponseEntity<ResponseWrapper> readByProjectCode(@PathVariable("projectcode") String projectcode){
        ProjectDTO projectDTO = projectService.getByProjectCode(projectcode);
        return ResponseEntity.ok(new ResponseWrapper("Project is retrieved", projectDTO));
    }

    @PostMapping
    @DefaultExceptionMessage(defaultMessage = "Could not create project!")
    @Operation(summary = "Create project")
    @PreAuthorize("hasAnyAuthority('Admin','Manager')")
    public ResponseEntity<ResponseWrapper> createProject(@RequestBody ProjectDTO projectDTO) throws TicketingProjectException {
        ProjectDTO createdProject = projectService.save(projectDTO);
        return ResponseEntity.ok(new ResponseWrapper("Project is created", createdProject));
    }

    @PutMapping
    @DefaultExceptionMessage(defaultMessage = "Could not update project!")
    @Operation(summary = "Update project")
    @PreAuthorize("hasAnyAuthority('Admin','Manager')")
    public ResponseEntity<ResponseWrapper> updateProject(@RequestBody ProjectDTO projectDTO) throws TicketingProjectException {
        ProjectDTO updatedProject = projectService.update(projectDTO);
        return ResponseEntity.ok(new ResponseWrapper("Project is updated", updatedProject));
    }

    @DeleteMapping("/{projectcode}")
    @DefaultExceptionMessage(defaultMessage = "Failed to delete project!")
    @Operation(summary = "Delete project")
    @PreAuthorize("hasAnyAuthority('Admin','Manager')")
    public ResponseEntity<ResponseWrapper> deleteProject(@PathVariable("projectcode") String projeccode) throws TicketingProjectException {
        projectService.delete(projeccode);
        return ResponseEntity.ok(new ResponseWrapper("Project is deleted"));
    }

    @PutMapping("/complete/{projectcode}")
    @DefaultExceptionMessage(defaultMessage = "Failed to complete project!")
    @Operation(summary = "Complete project")
    @PreAuthorize("hasAuthority('Manager')")
    public ResponseEntity<ResponseWrapper> completeProject(@PathVariable("projectcode") String projeccode) throws TicketingProjectException {
        ProjectDTO projectDTO = projectService.complete(projeccode);
        return ResponseEntity.ok(new ResponseWrapper("Project is completed", projectDTO));
    }

}

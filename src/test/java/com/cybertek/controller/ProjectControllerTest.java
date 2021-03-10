package com.cybertek.controller;

import com.cybertek.dto.ProjectDTO;
import com.cybertek.dto.RoleDTO;
import com.cybertek.dto.UserDTO;
import com.cybertek.enums.Gender;
import com.cybertek.enums.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final String token = "eyJhbGciOiJIUzI1NiJ9.eyJmaXJzdE5hbWUiOiJtaWtlIiwibGFzdE5hbWUiOiJzbWl0aCIsInN1YiI6Im1pY2hhZWw3NzdzZWxpdmFub3ZAZ21haWwuY29tIiwiaWQiOjIsInVzZXJOYW1lIjoibWljaGFlbDc3N3NlbGl2YW5vdkBnbWFpbC5jb20iLCJleHAiOjE2MTUzNzEzMzQsImlhdCI6MTYxNTMzNTMzNH0.gXwY0brGIE0sf3Z6KP2tecnRRQrmI2-okyL8b020iW8";

    static UserDTO userDTO;
    static ProjectDTO projectDTO;

    @BeforeAll
    static void setUp(){

        userDTO = UserDTO.builder()
                .id(2L)
                .firstName("mike")
                .lastName("smith")
                .userName("michael777selivanov@gamil.com")
                .passWord("abc123")
                .confirmPassword("abc123")
                .role(new RoleDTO(2L, "Manager"))
                .gender(Gender.MALE)
                .build();

        projectDTO = projectDTO.builder()
                .projectCode("SPRING01")
                .projectName("Test")
                .assignedManager(userDTO)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(5))
                .projectDetail("Api Test")
                .projectStatus(Status.OPEN)
                .completeTaskCounts(0)
                .unfinishedTaskCounts(0)
                .build();
    }

    @Test
    public void givenNoToken_whenGetSecureRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/project/v1/Api1"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenToken_getAllProjects() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/project")
                .header("Authorization",token)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].projectCode").exists()) // $.data[0].projectCode jayway jsonPath
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].assignedManager.userName").isNotEmpty());
    }

    @Test
    public void givenToken_createProject() throws Exception {
       mockMvc.perform(MockMvcRequestBuilders
       .post("/api/v1/project")
       .header("Authorization",token)
               .content(toJsonString(projectDTO))
       .contentType(MediaType.APPLICATION_JSON)
       .accept(MediaType.APPLICATION_JSON))
       .andExpect(MockMvcResultMatchers.jsonPath("$.data.projectCode").isNotEmpty());

    }

    @Test
    public void givenToken_updateProject() throws Exception {
        // need to provide project id since we didn't provide it when built projectDTO in before class
        projectDTO.setId(2L);

        // set to test update
        projectDTO.setProjectName("This is update Test");

        mockMvc.perform(MockMvcRequestBuilders
        .put("/api/v1/project")
        .header("Authorization", token)
        .content(toJsonString(projectDTO))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Project is updated"));

    }

    @Test
    public void givenToken_deleteProject() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
        .delete("/api/v1/project/" + projectDTO.getProjectCode())
        .header("Authorization", token)
        .content(toJsonString(projectDTO))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    protected String toJsonString(final Object obj){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
        objectMapper.registerModule(new JavaTimeModule());
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    // to get token dynamically with valid credentials
//    private String getToken(String userName, String password) throws Exception {
//        String json = mockMvc.perform(
//                MockMvcRequestBuilders
//                        .post("/authenticate")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .content("{" +
//                                "\"username\": \"" + userName + "\"," +
//                                "\"password\": \"" + password + "\"" +
//                                "}")
//        )
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse().getContentAsString();
//        return jsonParser.parseMap(json).get("data").toString();
//    }

}
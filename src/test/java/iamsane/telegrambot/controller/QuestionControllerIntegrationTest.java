package iamsane.telegrambot.controller;

import iamsane.telegrambot.dto.AnswerDto;
import iamsane.telegrambot.dto.QuestionDto;
import iamsane.telegrambot.entity.Question;
import iamsane.telegrambot.repository.QuestionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static iamsane.telegrambot.controller.QuestionController.Endpoints.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@Testcontainers
class QuestionControllerIntegrationTest {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private QuestionRepository questionRepository;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @AfterEach
    public void tearDown() {
        questionRepository.deleteAll();
    }

    @Test
    void createQuestion() {
        var questionDto = QuestionDto.builder()
                .questionText("Yes or no?")
                .answers(List.of(
                        AnswerDto.builder()
                                .answerText("Yes")
                                .build(),
                        AnswerDto.builder()
                                .answerText("No")
                                .build()
                ))
                .build();

        webTestClient.post()
                .uri(CREATE_QUESTION)
                .body(BodyInserters.fromValue(questionDto))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Long.class);
    }

    @Test
    void findQuestion() {
        Long id = questionRepository.save(new Question()).getId();

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(FIND_QUESTION)
                        .build(id))
                .exchange()
                .expectStatus().isOk()
                .expectBody(QuestionDto.class);
    }

    @Test
    void questionNotFound() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(FIND_QUESTION)
                        .build(1))
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void findAllQuestions() {
        questionRepository.save(new Question());

        webTestClient.get()
                .uri(FIND_QUESTIONS)
                .exchange()
                .expectStatus().isOk()
                .expectBody(List.class);
    }

    @Test
    void deleteQuestion() {
        Long id = questionRepository.save(new Question()).getId();

        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.
                        path(DELETE_QUESTION)
                        .build(id))
                .exchange()
                .expectStatus().isOk();

        Assertions.assertThrows(Exception.class, () -> {
            questionRepository.findById(id).orElseThrow();
        });
    }
}

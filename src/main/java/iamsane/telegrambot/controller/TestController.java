package iamsane.telegrambot.controller;

import iamsane.telegrambot.entity.Answer;
import iamsane.telegrambot.entity.Test;
import iamsane.telegrambot.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final TestRepository testRepository;

    @GetMapping("/api/v1/tests/{id}")
    public Test findTest(@PathVariable Long id) {
        return testRepository.findById(id).orElseThrow();
    }

    @GetMapping("/api/v1/tests")
    public List<Test> findTests() {
        return testRepository.findAll();
    }

    @GetMapping("/api/v1/tests/{id}/answers")
    public List<Answer> findTestAnswers(@PathVariable Long id) {
        return testRepository.findById(id)
                .orElseThrow()
                .getAnswers();
    }
}

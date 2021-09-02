package net.cho.api.quiz.Controller;

import lombok.RequiredArgsConstructor;
import net.cho.api.quiz.domain.Quiz;
import net.cho.api.quiz.service.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/quizzes")
public class QuizController {
    private final QuizService quizService;

    @GetMapping("/random")
    public ResponseEntity<Mono<Quiz>> getQuiz(){
        return ResponseEntity.ok(quizService.createQuiz());
    }
}

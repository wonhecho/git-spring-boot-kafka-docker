package net.cho.api.quiz.Controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.cho.api.quiz.domain.Attempt;
import net.cho.api.quiz.domain.Quiz;
import net.cho.api.quiz.service.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/results")

public class AttemptController {
    private final QuizService quizService;

    @RequiredArgsConstructor
    @NoArgsConstructor(force = true)
    @Getter
    static final class ResultResponse{
        private final boolean correct;
    }
    @PostMapping
    ResponseEntity<Attempt> postResult(@RequestBody Attempt attempt){
        boolean isCorrect = quizService.checkAttempt(attempt);
        Attempt attemptCopy = new Attempt(attempt.getUser(), attempt.getQuiz(), attempt.getResultAttempt(), isCorrect);
        return ResponseEntity.ok(attemptCopy);
    }
    @GetMapping
    ResponseEntity<Flux<Attempt>> getStatistics(@RequestParam("alias") String alias){
        return ResponseEntity.ok(quizService.getStatsForUser(alias));
    }
    @GetMapping("/{id}")
    ResponseEntity<Mono<Attempt>> getResultById(final @PathVariable("id") long id){
        return ResponseEntity.ok(quizService.getResultById(id));
    }

}

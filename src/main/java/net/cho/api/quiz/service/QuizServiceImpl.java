package net.cho.api.quiz.service;
import lombok.RequiredArgsConstructor;
import net.cho.api.quiz.domain.Attempt;
import net.cho.api.quiz.domain.Quiz;
import net.cho.api.quiz.domain.User;
import net.cho.api.quiz.repository.AttemptRepository;
import net.cho.api.quiz.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service @RequiredArgsConstructor
public class QuizServiceImpl implements QuizService{
    private final GeneratorService generatorService;
    private final UserRepository userRepository;
    private final AttemptRepository attemptRepository;
    @Override
    public Mono<Quiz> createQuiz() {
        int factorA = generatorService.randomFactor();
        int factorB = generatorService.randomFactor();
        Quiz quiz = new Quiz(factorA , factorB);
        return Mono.just(quiz);
    }

    @Override
    public boolean checkAttempt(Attempt attempt) {
        Optional<User> user = userRepository.findByAlias(attempt.getUser().getAlias());

        Assert.isTrue(!attempt.isCorrect(),"Unable to send in graded state");

        // Score
        boolean isCorrect = attempt.getResultAttempt() == attempt.getQuiz().getFactorA() *
                                                                attempt.getQuiz().getFactorB();
        // Create Copy
        Attempt checkAttempt = new Attempt(attempt.getUser(), attempt.getQuiz(), attempt.getResultAttempt(), isCorrect);

        // Store
        attemptRepository.save(checkAttempt);

        return isCorrect;
    }

    @Override
    public Flux<Attempt> getStatsForUser(String alias) {
        return attemptRepository.findTop5ByUserAliasOrderByIdDesc(alias);
    }

    @Override
    public Mono<Attempt> getResultById(long id) {
        return null;
    }
}

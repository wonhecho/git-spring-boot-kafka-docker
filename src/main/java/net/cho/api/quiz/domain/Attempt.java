package net.cho.api.quiz.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Getter
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode @Document(collation = "attempts")
public class Attempt implements Serializable {
//    @Id
//    private int id;
//    private final User user;
//    private final Quiz quiz;
//    private final boolean correct;
//    private final int resultAttempt;
    @Id
    private long id;
    private final User user;
    private final Quiz quiz;
    private final int resultAttempt;
    private final boolean correct;
}

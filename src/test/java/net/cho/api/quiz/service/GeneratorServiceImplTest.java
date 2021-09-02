package net.cho.api.quiz.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class GeneratorServiceImplTest {
    @Mock
    GeneratorService generatorService;
    @BeforeEach
    void setUp() {
        generatorService = new GeneratorServiceImpl();
    }
    @DisplayName("Check Valid Generator Service")
    @Test
    void randomFactor() {
//        List<Integer> randoms = IntStream.range(0,1000)

    }
}
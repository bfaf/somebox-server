package com.kchonov.someboxserver.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BonusPointsServiceTest {
    @InjectMocks
    private BonusPointsService bonusPointsService;

    @Test
    void firstExample() {
        double[] result = bonusPointsService.calculateBonusPoints(20);
        assertEquals(6, result[0], "First result is wrong");
        assertEquals(26, result[1], "Second result is wrong");
    }

    @Test
    void secondExample() {
        double[] result = bonusPointsService.calculateBonusPoints(175);
        assertEquals(Double.parseDouble("37.0"), result[0], "First result is wrong");
        assertEquals(Double.parseDouble("212.0"), result[1], "Second result is wrong");
    }

    @Test
    void thirdExample() {
        double[] result = bonusPointsService.calculateBonusPoints(2703);
        assertEquals(Double.parseDouble("270.3"), result[0], "First result is wrong");
        assertEquals(Double.parseDouble("2973.3"), result[1], "Second result is wrong");
    }

    @Test
    void fourthExample() {
        double[] result = bonusPointsService.calculateBonusPoints(15875);
        assertEquals(Double.parseDouble("1589.5"), result[0], "First result is wrong");
        assertEquals(Double.parseDouble("17464.5"), result[1], "Second result is wrong");
    }
}

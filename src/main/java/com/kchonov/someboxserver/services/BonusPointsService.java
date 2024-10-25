package com.kchonov.someboxserver.services;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BonusPointsService {

    public double getBonusPoints(int initialPoints) {
        if (initialPoints < 101) {
            return 5f;
        } else if (initialPoints > 100 && initialPoints < 1001) {
            return (initialPoints * 0.2f);
        } else { // more than 1000
            return (initialPoints * 0.1f);
        }
    }

    public int calculateAdditionalBonusPoints(int initialPoints) {
        if (initialPoints % 2 == 0) {
            return 1;
        } else if (initialPoints % 5 == 0) {
            return 2;
        } else {
            return 0;
        }
    }

    public double[] calculateBonusPoints(int initialPoints) {
        double bonusPoints = getBonusPoints(initialPoints);
        bonusPoints += calculateAdditionalBonusPoints(initialPoints);

        BigDecimal firstValue = new BigDecimal(bonusPoints)
                .setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal secondValue = new BigDecimal((initialPoints + bonusPoints))
                .setScale(2, RoundingMode.HALF_EVEN);

        return new double[] {
            firstValue.doubleValue(),
            secondValue.doubleValue()
        };
    }
}

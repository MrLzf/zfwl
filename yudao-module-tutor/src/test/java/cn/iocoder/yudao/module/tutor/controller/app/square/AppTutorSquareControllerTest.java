package cn.iocoder.yudao.module.tutor.controller.app.square;

import cn.iocoder.yudao.framework.test.core.ut.BaseMockitoUnitTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AppTutorSquareControllerTest extends BaseMockitoUnitTest {

    @Test
    void calculateDistance_roundsToOneDecimal() {
        BigDecimal distance = AppTutorSquareController.calculateDistance(
                new BigDecimal("116.397128"), new BigDecimal("39.916527"),
                new BigDecimal("116.407526"), new BigDecimal("39.904030"));

        assertEquals(new BigDecimal("1.7"), distance);
    }

    @Test
    void calculateDistance_whenNoLocation_returnsNull() {
        assertNull(AppTutorSquareController.calculateDistance(null, new BigDecimal("39.916527"),
                new BigDecimal("116.407526"), new BigDecimal("39.904030")));
    }
}

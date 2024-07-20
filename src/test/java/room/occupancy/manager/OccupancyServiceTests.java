package room.occupancy.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import room.occupancy.manager.ocuppancy.dto.OccupancyResult;
import room.occupancy.manager.ocuppancy.service.OccupancyService;
import room.occupancy.manager.ocuppancy.dto.CalculateOccupancyRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class OccupancyServiceTests {

    private OccupancyService occupancyService;

    @BeforeEach
    public void setUp() {
        occupancyService = new OccupancyService();
    }

    private static List<BigDecimal> getGuests() {
        return Arrays.asList(new BigDecimal("23.0"), new BigDecimal("45.0"), new BigDecimal("155.0"),
            new BigDecimal("374.0"), new BigDecimal("22.0"), new BigDecimal("99.99"), new BigDecimal("100.0"),
            new BigDecimal("101.0"), new BigDecimal("115.0"), new BigDecimal("209.0"));
    }

    private static Stream<Arguments> provideOccupancyScenarios() {
        return Stream.of(
            createTestArguments("Scenario 1: 3 Premium, 3 Economy rooms", 3, 3, 3, 3, new BigDecimal("738.0"), new BigDecimal("167.99")),
            createTestArguments("Scenario 2: 7 Premium, 5 Economy rooms", 7, 5, 6, 4, new BigDecimal("1054.0"), new BigDecimal("189.99")),
            createTestArguments("Scenario 3: 2 Premium, 7 Economy rooms", 2, 7, 2, 4, new BigDecimal("583.0"), new BigDecimal("189.99")),
            createTestArguments("Scenario 4: 7 Premium, 1 Economy rooms", 7, 1, 7, 1, new BigDecimal("1153.99"), new BigDecimal("45.0"))
        );    }

    private static Arguments createTestArguments(String description, int premiumRooms, int economyRooms,
        int expectedPremiumRooms, int expectedEconomyRooms, BigDecimal expectedPremiumEarnings, BigDecimal expectedEconomyEarnings) {
        CalculateOccupancyRequest request = new CalculateOccupancyRequest(premiumRooms, economyRooms, getGuests());

        return Arguments.of(description, request, expectedPremiumRooms, expectedEconomyRooms,
            expectedPremiumEarnings, expectedEconomyEarnings);
    }

    @DisplayName("Test Opmitime Occupancy")
    @ParameterizedTest(name = "{0}")
    @MethodSource("provideOccupancyScenarios")
    public void testOptimizeOccupancy(String testDescription, CalculateOccupancyRequest request,
        int expectedPremiumRooms, int expectedEconomyRooms, BigDecimal expectedPremiumEarnings, BigDecimal expectedEconomyEarnings) {

        OccupancyResult result = occupancyService.optimizeOccupancy(request);

        assertEquals(expectedPremiumRooms, result.usedPremiumRooms());
        assertEquals(expectedEconomyRooms, result.usedEconomyRooms());
        assertEquals(expectedPremiumEarnings, result.totalPremiumEarnings());
        assertEquals(expectedEconomyEarnings, result.totalEconomyEarnings());

    }
}
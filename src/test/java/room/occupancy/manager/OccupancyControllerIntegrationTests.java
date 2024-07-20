package room.occupancy.manager;

import java.math.BigDecimal;
import room.occupancy.manager.ocuppancy.dto.CalculateOccupancyRequest;
import room.occupancy.manager.ocuppancy.dto.OccupancyResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OccupancyControllerIntegrationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    List<BigDecimal> payments;

    CalculateOccupancyRequest request;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        payments = Arrays.asList(new BigDecimal("23.0"), new BigDecimal("45.0"), new BigDecimal("155.0"),
            new BigDecimal("374.0"), new BigDecimal("22.0"), new BigDecimal("99.99"), new BigDecimal("100.0"),
            new BigDecimal("101.0"), new BigDecimal("115.0"), new BigDecimal("209.0"));
    }

    private void testOptimizeOccupancy(int premiumRooms, int economyRooms, OccupancyResult expectedResult)
        throws Exception {

        request = new CalculateOccupancyRequest(premiumRooms, economyRooms, payments);
        String expectedJson = objectMapper.writeValueAsString(expectedResult);

        mockMvc.perform(post("/api/occupancy")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedJson));
    }

    @Test
    public void testOptimizeOccupancyWithVariousRequests() throws Exception {
        testOptimizeOccupancy(3, 3, new OccupancyResult(3, 3, new BigDecimal("738.0"), new BigDecimal("167.99")));
        testOptimizeOccupancy(7, 5, new OccupancyResult(6, 4, new BigDecimal ("1054.0"), new BigDecimal("189.99")));
        testOptimizeOccupancy(2, 7, new OccupancyResult(2, 4, new BigDecimal("583.0"), new BigDecimal("189.99")));
        testOptimizeOccupancy(7, 1, new OccupancyResult(7, 1, new BigDecimal("1153.99"), new BigDecimal("45.0")));
    }

    @Test
    public void testNegativeEconomyRooms() throws Exception {
        request = new CalculateOccupancyRequest(3, -1, payments);
        performPostAndExpectBadRequest(request, "{\"economyRooms\":\"You can't have negative numbers of economy rooms\"}");
    }

    @Test
    public void testNullGuestPayments() throws Exception {
        request = new CalculateOccupancyRequest(3, 3, null);
        performPostAndExpectBadRequest(request, "{\"guestPayments\":\"Guest payments cannot be empty\"}");
    }

    @Test
    public void testEmptyGuestPayments() throws Exception {
        request = new CalculateOccupancyRequest(3, 3, Collections.emptyList());
        performPostAndExpectBadRequest(request, "{\"guestPayments\":\"Guest payments cannot be empty\"}");
    }

    private void performPostAndExpectBadRequest(CalculateOccupancyRequest request, String expectedErrorMessage) throws Exception {
        mockMvc.perform(post("/api/occupancy")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(expectedErrorMessage));
    }
}

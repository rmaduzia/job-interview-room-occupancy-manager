package room.occupancy.manager;

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

    List<Double> payments;

    CalculateOccupancyRequest request;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        payments = Arrays.asList(23.0, 45.0, 155.0, 374.0, 22.0, 99.99, 100.0, 101.0, 115.0, 209.0);
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
        testOptimizeOccupancy(3, 3, new OccupancyResult(3, 3, 738.0, 167.99));
        testOptimizeOccupancy(7, 5, new OccupancyResult(6, 4, 1054.0, 189.99));
        testOptimizeOccupancy(2, 7, new OccupancyResult(2, 4, 583.0, 189.99));
        testOptimizeOccupancy(7, 1, new OccupancyResult(7, 1, 1153.99, 45.0));
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

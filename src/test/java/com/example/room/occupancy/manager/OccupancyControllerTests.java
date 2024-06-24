package com.example.room.occupancy.manager;

import com.example.room.occupancy.manager.ocuppancy.CalculateOccupancyRequest;
import com.example.room.occupancy.manager.ocuppancy.OccupancyController;
import com.example.room.occupancy.manager.ocuppancy.OccupancyResult;
import com.example.room.occupancy.manager.ocuppancy.OccupancyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OccupancyController.class)
public class OccupancyControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OccupancyService occupancyService;

    @Autowired
    private ObjectMapper objectMapper;

    List<Double> payments;

    @BeforeEach
    public void setUp() {
        payments = Arrays.asList(23.0, 45.0, 155.0, 374.0, 22.0, 99.99, 100.0, 101.0, 115.0, 209.0);
    }

    private void testOptimizeOccupancy(int premiumRooms, int economyRooms, OccupancyResult expectedResult)
        throws Exception {

        CalculateOccupancyRequest request = new CalculateOccupancyRequest(premiumRooms, economyRooms, payments);
        String expectedJson = objectMapper.writeValueAsString(expectedResult);

        when(occupancyService.optimizeOccupancy(request)).thenReturn(expectedResult);

        mockMvc.perform(post("/api/occupancy")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedJson));
    }

    @Test
    public void testOptimizeOccupancyWithVariousRequests() throws Exception {
        testOptimizeOccupancy(3, 3, new OccupancyResult(3, 3, 1153.0, 189.99));
        testOptimizeOccupancy(7, 5, new OccupancyResult(6, 4, 1054.0, 189.99));
        testOptimizeOccupancy(2, 7, new OccupancyResult(2, 4, 583.0, 189.99));
        testOptimizeOccupancy(7, 1, new OccupancyResult(7, 1, 1153.0, 189.99));
    }
}
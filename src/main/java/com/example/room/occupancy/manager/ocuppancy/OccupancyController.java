package com.example.room.occupancy.manager.ocuppancy;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/occupancy")
@RequiredArgsConstructor
public class OccupancyController {

    private final OccupancyService occupancyService;

    @PostMapping
    public OccupancyResult optimizeOccupancy(@RequestBody CalculateOccupancyRequest calculateOccupancyRequest) {
        return occupancyService.optimizeOccupancy(calculateOccupancyRequest);
    }

}

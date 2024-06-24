package com.example.room.occupancy.manager.ocuppancy;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Occupancy Calculator", description = "Room Manager Occupancy Calculator")
@RequestMapping("/api/occupancy")
@RequiredArgsConstructor
@Validated
public class OccupancyController {

    private final OccupancyService occupancyService;

    @Operation(summary = "Calculate Occupancy for Guests")
    @PostMapping
    public OccupancyResult optimizeOccupancy(@Valid @RequestBody CalculateOccupancyRequest calculateOccupancyRequest) {
        return occupancyService.optimizeOccupancy(calculateOccupancyRequest);
    }
}
package room.occupancy.manager.ocuppancy.controller;

import room.occupancy.manager.ocuppancy.dto.CalculateOccupancyRequest;
import room.occupancy.manager.ocuppancy.dto.OccupancyResult;
import room.occupancy.manager.ocuppancy.service.OccupancyService;
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
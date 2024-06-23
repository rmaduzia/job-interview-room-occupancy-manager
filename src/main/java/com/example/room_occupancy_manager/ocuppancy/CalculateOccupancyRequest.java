package com.example.room_occupancy_manager.ocuppancy;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class CalculateOccupancyRequest {

    private int premiumRooms;
    private int economyRooms;
    private List<Double> guestPayments;

}

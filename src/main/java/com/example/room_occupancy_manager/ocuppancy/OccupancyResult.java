package com.example.room_occupancy_manager.ocuppancy;

public record OccupancyResult(int usedPremiumRooms, int usedEconomyRooms,
                              double totalPremiumEarnings, double totalEconomyEarnings) {

}

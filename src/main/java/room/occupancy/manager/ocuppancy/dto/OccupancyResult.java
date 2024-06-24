package room.occupancy.manager.ocuppancy.dto;

public record OccupancyResult(int usedPremiumRooms, int usedEconomyRooms,
                              double totalPremiumEarnings, double totalEconomyEarnings) {

}

package room.occupancy.manager.ocuppancy.dto;

import java.math.BigDecimal;

public record OccupancyResult(int usedPremiumRooms, int usedEconomyRooms,
                              BigDecimal totalPremiumEarnings, BigDecimal totalEconomyEarnings) {

}

package room.occupancy.manager.ocuppancy.service;

import java.math.BigDecimal;
import room.occupancy.manager.ocuppancy.dto.CalculateOccupancyRequest;
import room.occupancy.manager.ocuppancy.dto.Guest;
import room.occupancy.manager.ocuppancy.dto.OccupancyResult;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import java.util.Comparator;

@Service
public class OccupancyService {

    private static final BigDecimal PREMIUM_THRESHOLD = new BigDecimal("100");

    public OccupancyResult optimizeOccupancy (CalculateOccupancyRequest calculateOccupancyRequest) {

        List<Guest> guestedSortedByWillingnessToPay = calculateOccupancyRequest.getGuestPayments().stream().map(Guest::new)
            .sorted(Comparator.comparing(Guest::willingnessToPay).reversed())
            .toList();

        OccupancyStats premiumStats = new OccupancyStats();
        OccupancyStats economyStats = new OccupancyStats();

        List<Guest> economyGuests = new ArrayList<>();

        assignPremiumGuests(guestedSortedByWillingnessToPay, calculateOccupancyRequest.getPremiumRooms(), premiumStats, economyGuests);
        assignEconomyGuests(economyGuests, calculateOccupancyRequest, premiumStats, economyStats);

        return new OccupancyResult(
            premiumStats.roomsOccupied,
            economyStats.roomsOccupied,
            premiumStats.totalEarnings,
            economyStats.totalEarnings
        );
    }

    private void assignPremiumGuests(List<Guest> guests, int availablePremiumRooms, OccupancyStats premiumStats, List<Guest> economyGuests) {
        for (Guest guest : guests) {
            if (guest.willingnessToPay().compareTo(PREMIUM_THRESHOLD) >= 0 && premiumStats.roomsOccupied < availablePremiumRooms) {
                premiumStats.roomsOccupied++;
                premiumStats.totalEarnings = premiumStats.totalEarnings.add(guest.willingnessToPay());
            } else if (guest.willingnessToPay().compareTo(PREMIUM_THRESHOLD) < 0) {
                economyGuests.add(guest);
            }
        }
    }

    private void assignEconomyGuests(List<Guest> economyGuests, CalculateOccupancyRequest request, OccupancyStats premiumStats, OccupancyStats economyStats) {

        int availablePremiumRooms = request.getPremiumRooms();
        int availableEconomyRooms = request.getEconomyRooms();

        for (Guest guest : economyGuests) {
            if (premiumStats.roomsOccupied < availablePremiumRooms && economyGuests.size() > availableEconomyRooms) {
                premiumStats.roomsOccupied++;
                premiumStats.totalEarnings = premiumStats.totalEarnings.add(guest.willingnessToPay());
            } else if (economyStats.roomsOccupied < availableEconomyRooms) {
                economyStats.roomsOccupied++;
                economyStats.totalEarnings = economyStats.totalEarnings.add(guest.willingnessToPay());
            }
        }
    }

    private static class OccupancyStats {
        int roomsOccupied = 0;
        BigDecimal totalEarnings = BigDecimal.valueOf(0);
    }
}
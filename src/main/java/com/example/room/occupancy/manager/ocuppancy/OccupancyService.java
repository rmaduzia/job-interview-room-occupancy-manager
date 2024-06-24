package com.example.room.occupancy.manager.ocuppancy;

import com.example.room.occupancy.manager.domain.Guest;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import java.util.Comparator;

@Service
public class OccupancyService {

    private static final double PREMIUM_THRESHOLD = 100.0;

    public OccupancyResult optimizeOccupancy (CalculateOccupancyRequest calculateOccupancyRequest) {

        List<Guest> sortedGuests = calculateOccupancyRequest.getGuestPayments().stream().map(Guest::new)
            .sorted(Comparator.comparingDouble(Guest::willingnessToPay).reversed())
            .toList();

        int usedPremiumRooms = 0;
        int usedEconomyRooms = 0;
        double totalPremiumEarnings = 0;
        double totalEconomyEarnings = 0;

        List<Guest> economyGuests = new ArrayList<>();

        for (Guest guest : sortedGuests) {
            if (guest.willingnessToPay() >= PREMIUM_THRESHOLD && usedPremiumRooms < calculateOccupancyRequest.getPremiumRooms()) {
                usedPremiumRooms++;
                totalPremiumEarnings += guest.willingnessToPay();
            } else if (guest.willingnessToPay() < PREMIUM_THRESHOLD) {
                economyGuests.add(guest);
            }
        }

        for (Guest guest : economyGuests) {
            if (usedPremiumRooms < calculateOccupancyRequest.getPremiumRooms() && economyGuests.size() > calculateOccupancyRequest.getEconomyRooms()) {
                usedPremiumRooms++;
                totalPremiumEarnings += guest.willingnessToPay();
            } else if (usedEconomyRooms < calculateOccupancyRequest.getEconomyRooms()) {
                usedEconomyRooms++;
                totalEconomyEarnings += guest.willingnessToPay();
            }
        }

            return new OccupancyResult(usedPremiumRooms, usedEconomyRooms, totalPremiumEarnings,
                totalEconomyEarnings);
    }
}
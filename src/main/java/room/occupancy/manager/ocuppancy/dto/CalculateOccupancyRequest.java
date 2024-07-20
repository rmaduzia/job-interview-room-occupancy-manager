package room.occupancy.manager.ocuppancy.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
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

    @Min(value = 0, message = "You can't have negative numbers of premium rooms")
    private int premiumRooms;
    @Min(value = 0, message = "You can't have negative numbers of economy rooms")
    private int economyRooms;

    @NotNull(message = "Guest payments cannot be empty")
    @NotEmpty(message = "Guest payments cannot be empty")
    private List<BigDecimal> guestPayments;
}
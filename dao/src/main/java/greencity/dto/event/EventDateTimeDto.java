package greencity.dto.event;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class EventDateTimeDto {
    private boolean isAllDay;

    private LocalTime startTime;

    private LocalTime endTime;

    private Integer dayCount;

    private LocalDate date;
}

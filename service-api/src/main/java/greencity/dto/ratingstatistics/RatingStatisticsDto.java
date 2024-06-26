package greencity.dto.ratingstatistics;

import greencity.annotations.RatingCalculationEnum;
import greencity.dto.user.UserVO;
import lombok.*;
import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Builder
public class RatingStatisticsDto {
    private Long id;
    private ZonedDateTime createDate;
    private RatingCalculationEnum ratingCalculationEnum;
    private float pointsChanged;
    private float rating;
    private UserVO user;
}

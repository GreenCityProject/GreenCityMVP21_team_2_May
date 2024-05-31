package greencity.dto.event;

import greencity.annotations.ValidEventDateTime;
import greencity.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class EventModelDto {
    //TODO need to be modified due to requirements and implementations
//    @Id
    private Long id;

    @NotBlank
    @Size(max = 70)
    private String title;

//    @OneToMany
    @NotNull
    @ValidEventDateTime
    //@ValidLocation
    @Size(max = 7, min = 1)
    private List<EventDayInfo> dayInfos;

    @NotBlank
    @Size(min = 20, max = 63206)
    private String description;

    private boolean isOpen = true;

//    @OneToMany
    private List<EventImage> images = new ArrayList<>();

    //@ManyToOne
    @NotNull
    private User author;
}

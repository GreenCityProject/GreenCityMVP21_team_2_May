package greencity.dto.eventcomment;

import greencity.annotations.NoProhibitedContentInComment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class EventCommentRequestDto {
    @NotBlank(message = "Comment cannot be blank")
    @NoProhibitedContentInComment
    @Size(min = 1, max = 8000)
    private String text;

    private Long parentCommentId;
}

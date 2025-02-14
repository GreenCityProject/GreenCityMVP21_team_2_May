package greencity.controller;

import greencity.annotations.ApiPageableWithoutSort;
import greencity.annotations.CurrentUser;
import greencity.constant.HttpStatuses;
import greencity.dto.PageableDto;
import greencity.dto.econewscomment.AddEcoNewsCommentDtoResponse;
import greencity.dto.eventcomment.EventCommentRequestDto;
import greencity.dto.eventcomment.EventCommentResponseDto;
import greencity.dto.user.UserManagementDto;
import greencity.dto.user.UserVO;
import greencity.exception.exceptions.BadRequestException;
import greencity.service.EventCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/events/{eventId}/comments")
public class EventCommentController {
    private final EventCommentService eventCommentService;

    /**
     * Method for creating {@link EventComment}.
     *
     * @param eventId id of {@link Event} to add comment to.
     * @param request   - dto for {@link EventComment} entity.
     * @return dto {@link EventCommentResponseDto}
     */
    @Operation(summary = "Add comment to event.")
    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = HttpStatuses.CREATED,
                    content = @Content(schema = @Schema(implementation = AddEcoNewsCommentDtoResponse.class))),
            @ApiResponse(responseCode = "303", description = HttpStatuses.SEE_OTHER),
            @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @PostMapping
    public ResponseEntity<EventCommentResponseDto> save(
            @PathVariable Long eventId,
            @Valid @RequestBody EventCommentRequestDto request,
            @Parameter(hidden = true) @CurrentUser UserVO user) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventCommentService.save(eventId, request, user));
    }

    /**
     * Method to count not deleted comments to certain {@link Event}.
     *
     * @param eventId to specify {@link Event}
     * @return amount of comments
     */
    @Operation(summary = "Count comments of event.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @GetMapping("/count")
    public int getCountOfComments(@PathVariable Long eventId) {
        return eventCommentService.countOfComments(eventId);
    }

    /**
     * Method to get all not deleted comments to {@link Event} specified by
     * eventId.
     *
     * @param eventId id of {@link Event}
     * @return Pageable of {@link EventCommentResponseDto}
     */
    @Operation(summary = "Get all comments of event.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @GetMapping
    @ApiPageableWithoutSort
    public ResponseEntity<PageableDto<EventCommentResponseDto>> getAllEventComments(
            @Parameter(hidden = true) Pageable pageable,
            @PathVariable Long eventId) {
        Field[] fields = EventCommentResponseDto.class.getDeclaredFields();
        List<String> fieldsNames = Arrays.stream(fields).map(Field::getName).toList();
        for(Sort.Order order : pageable.getSort()) {
            for (Field field: fields){
                if (!fieldsNames.contains(order.getProperty())) {
                    throw new BadRequestException(order.getProperty() + " property not exist");
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(eventCommentService.getAllEventComments(pageable, eventId));
    }

    /**
     * Method to update certain {@link EventComment} by id.
     *
     * @param commentId of {@link EventComment} to update
     * @param commentText edited text of {@link EventComment}
     * @author Roman Kasarab
     */
    @Operation(summary = "Update comment.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = HttpStatuses.NO_CONTENT),
            @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST,
                    content = @Content(examples = @ExampleObject(HttpStatuses.BAD_REQUEST))),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED,
                    content = @Content(examples = @ExampleObject(HttpStatuses.UNAUTHORIZED))),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND,
                    content = @Content(examples = @ExampleObject(HttpStatuses.NOT_FOUND)))
    })
    @PatchMapping("/{commentId}")
    public ResponseEntity<Object> update(
            @PathVariable Long eventId,
            @PathVariable Long commentId,
            @RequestBody @Size(min = 1, max = 8000) String commentText,
            @Parameter(hidden = true) Principal principal) {
        eventCommentService.update(eventId, commentId, commentText, principal.getName());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Method for deleting {EventComment} by its id.
     *
     * @param eventCommentId {@link EventComment} id
     *                       which will be deleted.
     * @return message of deleted comment {@link EventComment}.
     * @author Roman Kasarab
     */
    @Operation(summary = "Delete comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED,
                    content = @Content(examples = @ExampleObject(HttpStatuses.UNAUTHORIZED))),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND,
                    content = @Content(examples = @ExampleObject(HttpStatuses.NOT_FOUND)))
    })
    @DeleteMapping("/{eventCommentId}")
    public ResponseEntity<Object> delete(
            @PathVariable Long eventId,
            @PathVariable Long eventCommentId,
            @Parameter(hidden = true) Principal principal) {
        return ResponseEntity.status(HttpStatus.OK).body(eventCommentService.delete(eventId, eventCommentId, principal.getName()));
    }

    /**
     * Method to get event comment by comment id {@link Event} specified by
     * eventId and commentId.
     *
     * @param commentId id of {@link EventComment}
     * @return EventCommentResponseDto of {@link EventCommentResponseDto}
     */
    @Operation(summary = "Get comment to event by event id and comment id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @GetMapping("/{commentId}")
    @ApiPageableWithoutSort
    public ResponseEntity<EventCommentResponseDto> getByEventCommentId(
            @PathVariable Long eventId,
            @PathVariable Long commentId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(eventCommentService.getByEventCommentId(eventId, commentId));
    }
}

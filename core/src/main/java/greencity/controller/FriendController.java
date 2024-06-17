package greencity.controller;

import greencity.annotations.ApiLocale;
import greencity.annotations.ApiPageableWithLocale;
import greencity.annotations.CurrentUser;
import greencity.constant.HttpStatuses;
import greencity.dto.PageableDto;
import greencity.dto.friend.FriendDtoResponse;
import greencity.dto.user.UserVO;
import greencity.service.FriendService;
import greencity.service.FriendshipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/friends")
public class FriendController {
    private final FriendService friendService;
    private final FriendshipService friendshipService;

    @Operation(summary = "Get user's friends from the same city and with the same habits")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
        @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
        @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND),
    })
    @GetMapping("/{userId}")
    @ApiPageableWithLocale
    public ResponseEntity<PageableDto<FriendDtoResponse>> getAllFriendsByUserID(
            @PathVariable Long userId,
            @Parameter(hidden = true)
            @PageableDefault(size = 6, sort = "u.rating", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(
                friendService.getAllUserFriends(userId, pageable));
    }

    @Operation(summary = "Getting info about a friend with user attributes, habit counters, and mutual friends")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
        @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
        @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND),
    })
    @GetMapping("/profile/{userId}")
    @ApiLocale
    public ResponseEntity<FriendDtoResponse> getFriendProfile(
            @PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                friendService.getFriendProfile(userId));
    }

    @Operation(summary = "Search for friends with optional filters by city and by friends of friends")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
        @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
        @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND),
    })
    @GetMapping("/search/{userId}")
    @ApiPageableWithLocale
    public ResponseEntity<PageableDto<FriendDtoResponse>> getFriendByUserID(
        @PathVariable Long userId,
        @RequestParam(required = false) boolean filterByCity,
        @RequestParam(required = false, defaultValue = "false") boolean friendsOfFriends,
        @PageableDefault(sort = "u.rating", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(
                friendService.searchFriends(userId, filterByCity, friendsOfFriends, pageable));
    }

    @Operation(summary = "Adding a new friend")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
        @ApiResponse(responseCode = "201", description = HttpStatuses.CREATED),
        @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
        @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND),
    })
    @PostMapping("/{userId}")
    @ApiLocale
    public ResponseEntity<String> addFriend(
        @PathVariable Long userId,
        @RequestParam Long friendId,
        @Parameter(hidden = true) @CurrentUser UserVO user) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(friendshipService.addFriend(userId, friendId, user));
    }

    @Operation(summary = "Remove friend from friend list")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
        @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
        @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND),
    })
    @DeleteMapping("/delete")
    @ApiLocale
    public ResponseEntity<String> deleteFriend(
            @RequestParam("userId") Long userId,
            @RequestParam("friendId") Long friendId,
            @Parameter(hidden = true) @CurrentUser UserVO user) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(friendshipService.deleteFriend(userId, friendId, user));
    }
}

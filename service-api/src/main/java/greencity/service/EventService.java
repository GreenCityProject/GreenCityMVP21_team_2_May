package greencity.service;

import greencity.dto.PageableAdvancedDto;
import greencity.dto.event.EventAttendanceDto;
import greencity.dto.event.EventRequestSaveDto;
import greencity.dto.event.EventResponseDto;
import greencity.dto.event.EventUpdateRequestDto;
import greencity.dto.user.UserVO;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface EventService {
    EventResponseDto save(EventRequestSaveDto event, MultipartFile[] images, UserVO author);

    EventResponseDto update(EventUpdateRequestDto eventUpdateRequestDto, MultipartFile[] images, String email);

    void delete(Long id, String email);

    //Pageable implements sorted criteria
    PageableAdvancedDto<EventResponseDto> findAll(Pageable pageable);

    EventResponseDto findById(Long id);

    String uploadImage(MultipartFile image);

    //need consider if we need both below and above
    String[] uploadImages(MultipartFile[] images);

    PageableAdvancedDto<EventResponseDto> findAllByAuthor(Pageable pageable, Long userId);

    /**
     * Add an attender to the Event by id.
     *
     * @param eventId - event id.
     * @param user - user.
     */
    void addAttender(Long eventId, UserVO user);

    /**
     * Get all attenders of the Event by eventId.
     *
     * @param eventId - event id.
     * @return a Set of Event Attenders
     */
    Set<EventAttendanceDto> findAllAttendersByEvent(Long eventId);

    /**
     * Remove an attender from the Event by id.
     *
     * @param eventId - event id.
     * @param email   - user`s email.
     * @return
     */
    String removeAttender(Long eventId, String email);

}

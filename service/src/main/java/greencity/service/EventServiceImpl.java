package greencity.service;

import greencity.client.RestClient;
import greencity.constant.ErrorMessage;
import greencity.dto.PageableAdvancedDto;
import greencity.dto.event.*;
import greencity.entity.Tag;
import greencity.entity.event.EventAddress;
import greencity.entity.event.EventDayInfo;
import greencity.entity.event.EventImage;
import greencity.entity.event.Event;
import greencity.dto.tag.TagVO;
import greencity.dto.user.UserVO;
import greencity.entity.User;
import greencity.enums.Role;
import greencity.enums.TagType;
import greencity.exception.exceptions.BadRequestException;
import greencity.exception.exceptions.NotFoundException;
import greencity.exception.exceptions.NotSavedException;
import greencity.exception.exceptions.UserHasNoPermissionToAccessException;
import greencity.exception.exceptions.WrongIdException;
import greencity.message.EventEmailMessage;
import greencity.repository.EventRepo;
import greencity.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

@Service
@EnableCaching
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepo eventRepo;
    private final ModelMapper modelMapper;
    private final FileService fileService;
    private final UserRepo userRepo;
    private final UserService userService;
    private final RestClient restClient;
    private final TagsService tagsService;
    private final ThreadPoolExecutor emailThreadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);


    @Override
    public EventResponseDto save(EventRequestSaveDto event, MultipartFile[] images, UserVO author) {
        String[] uploadedImages = uploadImages(images);

        List<EventImage> eventImages = new ArrayList<>();
        if (event.getMainImageNumber() == 0) {
            event.setMainImageNumber(1);
        }
        if (images != null && images.length > 0) {
            for (int i = 0; i < uploadedImages.length; i++) {
                eventImages.add(EventImage.builder().imagePath(uploadedImages[i]).isMain(false).build());

            }
            eventImages.get(event.getMainImageNumber() - 1).setMain(true);
        }

        Event eventToSave = modelMapper.map(event, Event.class);
        eventToSave.setImages(eventImages);
        User user = modelMapper.map(author, User.class);
        eventToSave.setAuthor(user);


        if (event.getTags() != null && event.getTags().size() > 0) {
            if (new HashSet<>(event.getTags()).size() < event.getTags().size()) {
                throw new NotSavedException(ErrorMessage.EVENT_NOT_SAVED);
            }
            List<TagVO> tagVOS = tagsService.findTagsByNamesAndType(
                    event.getTags(), TagType.EVENT);
            eventToSave.setTags(modelMapper.map(tagVOS,
                    new TypeToken<List<Tag>>() {
                    }.getType()));
        }

        eventToSave = eventRepo.save(eventToSave);

        EventEmailMessage eventEmailMessage = modelMapper.map(eventToSave, EventEmailMessage.class);
        sendEmailNotification(eventEmailMessage);

        return modelMapper.map(eventToSave, EventResponseDto.class);
    }

    @Override
    public EventResponseDto updateEvent(
            EventUpdateRequestDto eventUpdateRequestDto,
            MultipartFile[] images,
            String email
    ) {
        UserVO userVO = userService.findByEmail(email);
        Event event = eventRepo.findById(eventUpdateRequestDto.getId())
                .orElseThrow(() -> new NotFoundException(ErrorMessage.EVENT_NOT_FOUND_BY_ID + eventUpdateRequestDto.getId()));

        if (!event.getAuthor().getId().equals(userVO.getId()) && userVO.getRole() != Role.ROLE_ADMIN) {
            throw new UserHasNoPermissionToAccessException("User has no permission to edit this event");
        }

        if (event.getDayInfos().stream().anyMatch(
                d -> d.getStartDateTime().isBefore(ChronoZonedDateTime.from(LocalDateTime.now())))) {
            throw new IllegalArgumentException("Cannot edit past events");
        }

        event.setTitle(eventUpdateRequestDto.getTitle());
        event.setDescription(eventUpdateRequestDto.getDescription());
        event.setOpen(eventUpdateRequestDto.getIsOpen());

        updateDayInfos(event, eventUpdateRequestDto.getDateTimes());
        updateImages(event, eventUpdateRequestDto.getImages());
        updateTags(event, eventUpdateRequestDto.getTagNames());

        eventRepo.save(event);
        return modelMapper.map(event, EventResponseDto.class);
    }

    private void updateDayInfos(Event event, List<EventResponseDayInfoDto> eventResponseDayInfoDto) {
        List<EventDayInfo> dayInfos = eventResponseDayInfoDto.stream()
                .map(dto -> modelMapper.map(dto, EventDayInfo.class))
                .collect(Collectors.toList());

        event.getDayInfos().clear();
        event.getDayInfos().addAll(dayInfos);
    }

    private void updateImages(Event event, List<EventImageDto> images) {
        List<EventImage> updatedImages = images.stream()
                .map(imgDto -> modelMapper.map(imgDto, EventImage.class))
                .collect(Collectors.toList());

        event.getImages().clear();
        event.getImages().addAll(updatedImages);
    }

    private void updateTags(Event event, List<String> tagNames) {
        List<Tag> tags = modelMapper.map(
                tagsService.findTagsByNamesAndType(tagNames, TagType.EVENT),
                new TypeToken<List<Tag>>() {
                }.getType());

        event.getTags().clear();
        event.getTags().addAll(tags);
    }

    @Override
    public void delete(Long id, String email) {
        UserVO userVO = userService.findByEmail(email);
        Event toDelete = eventRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.EVENT_NOT_FOUND_BY_ID + id));

        if (toDelete.getAuthor().getId().equals(userVO.getId()) || userVO.getRole() == Role.ROLE_ADMIN) {
            eventRepo.delete(toDelete);
        } else {
            throw new UserHasNoPermissionToAccessException(ErrorMessage.USER_HAS_NO_PERMISSION);
        }
    }

    @Override
    public EventResponseDto update(EventRequestSaveDto event, List<MultipartFile> images, UserVO author) {
        return null;
    }

    @Override
    public PageableAdvancedDto<EventResponseDto> findAll(Pageable pageable) {
        Page<Event> events = eventRepo.findAll(pageable);
        return buildPageableAdvancedDto(events);
    }

    @Override
    public EventResponseDto findById(Long id) {
        Event event = eventRepo
                .findById(id)
                .orElseThrow(() -> new WrongIdException(ErrorMessage.EVENT_NOT_FOUND_BY_ID + id));
        return modelMapper.map(event, EventResponseDto.class);
    }

    @Override
    public String uploadImage(MultipartFile image) {
        if (image != null) {
            return fileService.upload(image);
        }
        return null;
    }

    @Override
    public String[] uploadImages(MultipartFile[] images) {
        if (images != null) {
            return Arrays.stream(images).map(fileService::upload).toArray(String[]::new);
        }
        return new String[0];
    }

    @Override
    public PageableAdvancedDto<EventResponseDto> findAllByAuthor(Pageable pageable, Long userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new WrongIdException(ErrorMessage.USER_NOT_FOUND_BY_ID + userId));
        Page<Event> events = eventRepo.findAllByAuthorId(pageable, userId);
        return buildPageableAdvancedDto(events);
    }

    @Override
    public void addAttender(Long eventId, UserVO user) {
        Event event = eventRepo.findById(eventId).orElseThrow(() -> new NotFoundException(ErrorMessage.EVENT_NOT_FOUND_BY_ID + eventId));
        User currentUser = modelMapper.map(user, User.class);
        checkingJoiningEvent(event, currentUser);
        event.getAttenders().add(currentUser);
        eventRepo.save(event);
        EventEmailMessage eventEmailMessage = modelMapper.map(event, EventEmailMessage.class);
        eventEmailMessage.setSubject(String.format("User %s joined to your event", currentUser.getName()));
        sendEmailNotification(eventEmailMessage);
    }

    private void checkingJoiningEvent(Event event, User user) {
        if (event.getAuthor().getId().equals(user.getId())) {
            throw new BadRequestException(ErrorMessage.YOU_ARE_EVENT_AUTHOR);
        }
        if (!event.isOpen()) {
            throw new BadRequestException(ErrorMessage.YOU_CANNOT_JOIN_TO_CLOSED_EVENT);
        }
        if (event.getAttenders().stream().anyMatch(attender -> attender.getId().equals(user.getId()))) {
            throw new BadRequestException(ErrorMessage.YOU_HAVE_ALREADY_JOINED_TO_EVENT);
        }
    }

    public void sendEmailNotification(EventEmailMessage eventEmailMessage) {
        RequestAttributes originalRequestAttributes = RequestContextHolder.getRequestAttributes();
        emailThreadPool.submit(() -> {
            try {
                RequestContextHolder.setRequestAttributes(originalRequestAttributes);
                restClient.sendEmailNotification(eventEmailMessage);
            } finally {
                RequestContextHolder.resetRequestAttributes();
            }
        });
    }

    private PageableAdvancedDto<EventResponseDto> buildPageableAdvancedDto(Page<Event> eventPage) {
        List<EventResponseDto> eventResponseDtos = eventPage.stream()
                .map(event -> modelMapper.map(event, EventResponseDto.class))
                .toList();

        return new PageableAdvancedDto<>(
                eventResponseDtos,
                eventPage.getTotalElements(),
                eventPage.getPageable().getPageNumber(),
                eventPage.getTotalPages(),
                eventPage.getNumber(),
                eventPage.hasPrevious(),
                eventPage.hasNext(),
                eventPage.isFirst(),
                eventPage.isLast());
    }
}

package greencity.service;

import greencity.constant.ErrorMessage;
import greencity.dto.event.*;
import greencity.dto.event.model.EventImage;
import greencity.dto.event.model.EventModelDto;
import greencity.dto.user.UserVO;
import greencity.exception.exceptions.WrongIdException;
import greencity.repository.EventRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@EnableCaching
@RequiredArgsConstructor
public class EventServiceImpl implements EventService{
    private final EventRepo eventRepo;
    private final ModelMapper modelMapper;
    private final FileService fileService;

    @Override
    public EventResponseDto save(EventRequestSaveDto event, MultipartFile[] images, UserVO author) {
        String[] uploadedImages = uploadImages(images);

        List<EventImage> eventImages = new ArrayList<>();
        if (event.getMainImageNumber() == 0) {
            event.setMainImageNumber(1);
        }
        if (images.length > 0) {
            for (int i = 0; i < uploadedImages.length; i++) {
                eventImages.add(EventImage.builder().imagePath(uploadedImages[i]).isMain(false).build());

            }
            eventImages.get(event.getMainImageNumber() - 1).setMain(true);
        }

        EventModelDto eventModelDto = modelMapper.map(event, EventModelDto.class);
        eventModelDto.setImages(eventImages);
        eventModelDto.setAuthor(author);
        eventModelDto = eventRepo.save(eventModelDto);
        return modelMapper.map(eventModelDto, EventResponseDto.class);
    }

    @Override
    public void delete(Long id, UserVO author) {}

    @Override
    public EventModelDto update(EventRequestSaveDto event, List<MultipartFile> images, UserVO author) {
        return null;
    }

    @Override
    public List<EventResponseDto> findAll(Pageable pageable) {
        List<EventModelDto> eventModelDtos = eventRepo.findAll(pageable);
        return eventModelDtos.stream().map(e -> modelMapper.map(e, EventResponseDto.class)).toList();
    }

    @Override
    public EventResponseDto findById(Long id) {
        EventModelDto event = eventRepo
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
    public List<EventResponseDto> findAllByAuthor(Pageable pageable, Long userId) {
        List<EventModelDto> eventModelDtos = eventRepo
                .findAllByAuthorId(pageable, userId)
                .orElseThrow(() -> new WrongIdException(ErrorMessage.EVENT_NOT_FOUND_BY_AUTHOR_ID + userId));
        return eventModelDtos.stream().map(e -> modelMapper.map(e, EventResponseDto.class)).toList();
    }
}

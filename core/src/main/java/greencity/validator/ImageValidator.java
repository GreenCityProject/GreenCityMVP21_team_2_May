package greencity.validator;

import greencity.annotations.ImageValidation;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class ImageValidator implements ConstraintValidator<ImageValidation, MultipartFile> {
    private final List<String> validType = Arrays.asList("image/jpeg", "image/png", "image/jpg");

    @Override
    public void initialize(ImageValidation constraintAnnotation) {
        // Initializes the validator in preparation for #isValid calls
    }

    @Override
    public boolean isValid(MultipartFile image, ConstraintValidatorContext constraintValidatorContext) {
        if (image == null) {
            return true;
        } else {
            long maxSize = 10 * 1024 * 1024; //10mb
            if (image.getSize() > maxSize) {
                return false;
            }
            return validType.contains(image.getContentType());
        }
    }
}

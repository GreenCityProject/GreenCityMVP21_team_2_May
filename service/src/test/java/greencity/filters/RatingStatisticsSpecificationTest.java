package greencity.filters;

import greencity.entity.RatingStatistics;
import greencity.entity.RatingStatistics_;
import greencity.entity.User;
import greencity.entity.User_;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RatingStatisticsSpecificationTest {
    @Mock
    private Root<RatingStatistics> root;
    @Mock
    private CriteriaQuery<?> criteriaQuery;
    @Mock
    private CriteriaBuilder criteriaBuilder;
    @Mock
    private Predicate expected;
    @Mock
    private Path<Object> objectPath;
    @Mock
    private Path<String> stringPath;
    @Mock
    private Join<RatingStatistics, User> userJoin;

    private List<SearchCriteria> searchCriteriaList;
    private RatingStatisticsSpecification ratingStatisticsSpecification;

    @BeforeEach
    void init() {
        searchCriteriaList = new ArrayList<>();
        searchCriteriaList.add(SearchCriteria.builder()
                .key("id")
                .type("id")
                .value(1L)
                .build());
        searchCriteriaList.add(SearchCriteria.builder()
                .key("userMail")
                .type("userMail")
                .value("test@mail.com")
                .build());

        ratingStatisticsSpecification = new RatingStatisticsSpecification(searchCriteriaList);
    }

    @Test
    void toPredicate() {
        when(criteriaBuilder.conjunction()).thenReturn(expected);
        when(root.get("id")).thenReturn(objectPath);
        when(criteriaBuilder.equal(objectPath, 1L)).thenReturn(expected);
        when(root.join(RatingStatistics_.user)).thenReturn(userJoin);
        when(userJoin.get(User_.email)).thenReturn(stringPath);
        when(criteriaBuilder.like(stringPath, "%test@mail.com%")).thenReturn(expected);
        when(criteriaBuilder.and(expected, expected)).thenReturn(expected);

        Predicate actual = ratingStatisticsSpecification.toPredicate(root, criteriaQuery, criteriaBuilder);
        assertEquals(expected, actual);
    }
}

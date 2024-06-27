package greencity.mapping;

import greencity.entity.friend.Friendship;
import greencity.entity.friend.FriendshipId;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class FriendshipMapper {
    public static Friendship toEntity(Long userId, Long friendId, String status) {
        Friendship friendship = new Friendship();
        friendship.setPk(new FriendshipId(userId, friendId));
        friendship.setStatus(status);
        friendship.setCreatedDate(LocalDateTime.now());
        return friendship;
    }
}

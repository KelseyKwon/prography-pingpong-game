package prography.pingpong.domain.entity.room;

import jakarta.persistence.*;
import lombok.*;
import prography.pingpong.domain.entity.BaseTimeEntity;

@Getter
@Entity
@Table(name = "USER_ROOMS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ROOM_ID")
    private Integer id;

    @Column(name = "ROOM_ID", nullable = false)
    private Integer roomId;

    @Column(name = "USER_ID", nullable = false)
    private Integer userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "TEAM", nullable = false)
    private Team team;

    @Embedded
    private BaseTimeEntity baseTime = new BaseTimeEntity();

    @Builder
    public UserRoom(Integer roomId, Integer userId, Team team) {
        this.roomId = roomId;
        this.userId = userId;
        this.team = team;
    }

    public static UserRoom createUserRoom(Integer roomId, Integer userId, Team team) {
        return UserRoom.builder()
                .roomId(roomId)
                .userId(userId)
                .team(team)
                .build();
    }

    public void changeTeam() {
        this.team = (this.team == Team.RED) ? Team.BLUE : Team.RED;
        this.baseTime.updateTimestamp();
    }
}

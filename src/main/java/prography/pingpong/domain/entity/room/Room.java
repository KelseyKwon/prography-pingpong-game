package prography.pingpong.domain.entity.room;

import jakarta.persistence.*;
import lombok.*;
import prography.pingpong.domain.entity.BaseTimeEntity;
import prography.pingpong.domain.entity.user.User;


@Getter
@Entity
@Table(name = "ROOMS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room {
    @Id
    @GeneratedValue
    @Column(name = "ROOM_ID")
    private Integer id;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HOST_ID", nullable = false)
    private User host;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROOM_TYPE", nullable = false)
    private RoomType roomtype;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private RoomStatus status;

    @Embedded
    private BaseTimeEntity baseTime = new BaseTimeEntity();

    @Builder
    public Room(Integer id, String title, User host, RoomType roomtype, RoomStatus status, BaseTimeEntity baseTime) {
        this.id = id;
        this.title = title;
        this.host = host;
        this.roomtype = roomtype;
        this.status = status;
        this.baseTime = new BaseTimeEntity();
    }

    public static Room create(String title, User host, RoomType type) {
        return Room.builder()
                .title(title)
                .host(host)
                .roomtype(type)
                .status(RoomStatus.WAIT)
                .build();
    }

    public void changeStatus(RoomStatus status) {
        this.status = status;
        this.baseTime.updateTimestamp();
    }

    /** 게임 시작 */
    public void startGame() {
        this.status = RoomStatus.PROGRESS;
        this.baseTime.updateTimestamp();
    }

    /** 게임 종료 */
    public void finishGame() {
        this.status = RoomStatus.FINISH;
        this.baseTime.updateTimestamp();
    }
}

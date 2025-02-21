package prography.pingpong.domain.entity.user;

import jakarta.persistence.*;
import lombok.*;
import prography.pingpong.domain.entity.BaseTimeEntity;

@Getter @Setter
@Entity
@Table(name = "USERS", uniqueConstraints = {
        @UniqueConstraint(columnNames = "fakerId")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Integer id;

    @Column(nullable = false, unique = true)
    private Integer fakerId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @Embedded
    private BaseTimeEntity baseTime = new BaseTimeEntity();

    @Builder
    public User(Integer fakerId, String name, String email, UserStatus status, BaseTimeEntity baseTime) {
        this.fakerId = fakerId;
        this.name = name;
        this.email = email;
        this.status = status;
        this.baseTime = new BaseTimeEntity();
    }

    public static User createUser(Integer fakerId, String name, String email, UserStatus status) {
        return User.builder()
                .fakerId(fakerId)
                .name(name)
                .email(email)
                .status(UserStatus.determineStatus(fakerId))
                .build();
    }

    public void updateStatus(UserStatus status) {
        this.status = status;
        this.baseTime.updateTimestamp();
    }
}

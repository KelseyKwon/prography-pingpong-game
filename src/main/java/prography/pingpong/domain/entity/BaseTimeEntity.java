package prography.pingpong.domain.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Embeddable
public class BaseTimeEntity {

    private LocalDateTime createdat;
    private LocalDateTime updatedat;

    public BaseTimeEntity() {
        this.createdat = LocalDateTime.now();
        this.updatedat = LocalDateTime.now();
    }

    public void updateTimestamp() {
        this.updatedat = LocalDateTime.now();
    }
}


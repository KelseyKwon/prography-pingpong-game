package prography.pingpong.domain.dto.init;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class InitRequest {
    private Integer seed;
    private Integer quantity;
}

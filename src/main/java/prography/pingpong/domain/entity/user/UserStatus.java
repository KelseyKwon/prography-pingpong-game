package prography.pingpong.domain.entity.user;

import lombok.Getter;

@Getter
public enum UserStatus {
    WAIT,
    ACTIVE,
    NON_ACTIVE;

    /** fakerId를 기반으로 상태를 결정하는 메서드 */
    public static UserStatus determineStatus(int fakerId) {
        if (fakerId <= 30) {
            return ACTIVE;
        }
        if (fakerId <= 60) {
            return WAIT;
        }
        return NON_ACTIVE;
    }
}
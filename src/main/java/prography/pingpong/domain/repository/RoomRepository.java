package prography.pingpong.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import prography.pingpong.domain.entity.room.Room;
import prography.pingpong.domain.entity.room.RoomStatus;
import prography.pingpong.domain.entity.user.User;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    boolean existsByHostIdAndStatusNot(int hostId, RoomStatus status);

    /** WAIT 방을 ID로 조회하는 메서드 */
    Optional<Room> findByIdAndStatus(Integer id, RoomStatus status);
    /** 호스트가 특정 방의 주인인지 확인하는 메서드 */
    boolean existsByIdAndHost(Integer id, User host);
}


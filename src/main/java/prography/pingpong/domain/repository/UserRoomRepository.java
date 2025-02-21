package prography.pingpong.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import prography.pingpong.domain.entity.room.Team;
import prography.pingpong.domain.entity.room.UserRoom;

import java.util.List;

@Repository
public interface UserRoomRepository extends JpaRepository<UserRoom, Integer> {
    int countByRoomId(int roomId);
    int countByRoomIdAndTeam(int roomId, Team team);

    boolean existsByUserId(int userId);
    boolean existsByUserIdAndRoomId(int userId, int roomId);  // 특정 방 참가 여부 확인

    void deleteByUserId(int userId);
    void deleteByUserIdAndRoomId(int userId, int roomId);  // 특정 방에서만 삭제

    List<UserRoom> findAllByRoomId(int roomId);
}

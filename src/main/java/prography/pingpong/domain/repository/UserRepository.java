package prography.pingpong.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import prography.pingpong.domain.entity.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByFakerId(Integer fakerId);  // 중복 확인을 위한 메서드 추가
}

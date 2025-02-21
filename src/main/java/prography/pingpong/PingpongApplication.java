package prography.pingpong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "prography.pingpong.domain.entity")  // 엔티티 패키지 명확하게 지정
@EnableJpaRepositories(basePackages = "prography.pingpong.domain.repository")  // JPA 리포지토리 패키지 명확하게 지정
public class PingpongApplication {

	public static void main(String[] args) {
		SpringApplication.run(PingpongApplication.class, args);
	}

}

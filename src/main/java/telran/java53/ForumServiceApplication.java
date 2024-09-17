package telran.java53;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"telran.java53"})
@EnableJpaRepositories(basePackages = {"telran.java53.post.dao", "telran.java53.registration.token", "telran.java53.accounting.dao"})
@EntityScan(basePackages = {"telran.java53.post.model", "telran.java53.registration.token", "telran.java53.accounting.model"})

public class ForumServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForumServiceApplication.class, args);
	}

}

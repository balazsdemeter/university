package hu.cubix.university;

import hu.cubix.university.service.InitDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@RequiredArgsConstructor
@SpringBootApplication
public class UniversityApplication implements CommandLineRunner {

	private final InitDbService initDbService;

	public static void main(String[] args) {
		SpringApplication.run(UniversityApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		initDbService.deleteDb();
		initDbService.addInitData();
	}
}

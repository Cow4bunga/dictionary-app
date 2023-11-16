package by.levitsky.spring.dictionaryservice;


import by.levitsky.spring.dictionaryservice.service.FileService;
import jakarta.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TextServiceApplication implements CommandLineRunner {
	@Resource
	FileService fileService;
	public static void main(String[] args) {
		SpringApplication.run(TextServiceApplication.class, args);
	}

	@Override
	public void run(String... arg) throws Exception {
		fileService.init();
	}
}

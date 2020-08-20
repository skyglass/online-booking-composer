package skyglass.composer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBoot2RestServiceApplication {//extends SpringBootServletInitializer {

	//	@Override
	//	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	//		return application.sources(SpringBoot2RestServiceApplication.class);
	//	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBoot2RestServiceApplication.class, args);
	}
}

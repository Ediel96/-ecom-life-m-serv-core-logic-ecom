package life_ecom_logic_core.eddie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "life_ecom_logic_core.eddie",
        "com.backend.organize_life" // include generated API controllers
})
public class EddieApplication {

	public static void main(String[] args) {
		SpringApplication.run(EddieApplication.class, args);
	}

}

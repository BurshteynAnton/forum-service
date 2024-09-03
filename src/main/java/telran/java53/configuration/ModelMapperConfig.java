package telran.java53.configuration;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import telran.java53.accounting.service.UserAccountService;

@AllArgsConstructor
@EnableWebSecurity
@Configuration
public class ModelMapperConfig {

//	private final UserAccountService userAccountService;


	@Bean
	ModelMapper getModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration()
					.setMatchingStrategy(MatchingStrategies.STRICT)
					.setFieldMatchingEnabled(true)
					.setFieldAccessLevel(AccessLevel.PRIVATE);
		return modelMapper;
	}

}

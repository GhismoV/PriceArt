package it.ghismo.corso1.priceart.appconf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties("application")
@Data
public class AppConfig
{
	private String listino;
}

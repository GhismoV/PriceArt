package it.ghismo.corso1.priceart.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.ghismo.corso1.priceart.adapters.LocalDateTimeSerializer;
import it.ghismo.corso1.priceart.dto.ResultDto;
import it.ghismo.corso1.priceart.errors.ResultEnum;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthEntryPoint extends BasicAuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		//String err = "Utente e/o pwd errati!";
		log.warn("Errore di sicurezza: " + authException.getLocalizedMessage());
		
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName() + "");
		
		ResultDto out = ResultEnum.AuthenticationError.getDto();
		Gson gson = new GsonBuilder()
				.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
				.create();
		PrintWriter writer = response.getWriter();
		writer.println(gson.toJson(out));
	}

	@Override
	public void afterPropertiesSet() {
		setRealmName(SecurityConfiguration.REALM);
		super.afterPropertiesSet();
	}
	
	

}

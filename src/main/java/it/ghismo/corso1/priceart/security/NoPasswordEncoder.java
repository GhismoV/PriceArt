package it.ghismo.corso1.priceart.security;

import org.springframework.security.crypto.password.PasswordEncoder;

public class NoPasswordEncoder implements PasswordEncoder {

	@Override
	public String encode(CharSequence rawPassword) {
		return rawPassword != null ? rawPassword.toString() : null;
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return rawPassword == encodedPassword || (rawPassword != null && rawPassword.toString().equals(encodedPassword));
	}

}

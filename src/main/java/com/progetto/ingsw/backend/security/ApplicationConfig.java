package com.progetto.ingsw.backend.security;

import com.progetto.ingsw.backend.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfig {

    @Autowired
    private UtenteRepository utenteRepository;

    //  trovare l'utente nel DB usando l'email
    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            var utente = utenteRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato: " + email));

            // Trasformiamo il ruolo stringa (es. "ADMIN") in una GrantedAuthority (es. "ROLE_ADMIN")
            return org.springframework.security.core.userdetails.User.builder()
                    .username(utente.getEmail())
                    .password(utente.getPassword())
                    .authorities("ROLE_" + utente.getRuolo().toUpperCase())
                    .build();
        };
    }

    // Creiamo il Bean che mancava (AuthenticationProvider)
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // l'algoritmo per criptare le password (BCrypt è lo standard)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // gestisce il login nel controller
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

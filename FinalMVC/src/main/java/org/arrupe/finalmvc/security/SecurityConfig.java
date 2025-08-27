package org.arrupe.finalmvc.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpMethod;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final CustomLoginSuccessHandler loginSuccessHandler;

    public SecurityConfig(UserDetailsService userDetailsService,
                          CustomLoginSuccessHandler loginSuccessHandler) {
        this.userDetailsService = userDetailsService;
        this.loginSuccessHandler = loginSuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // ✅ Accesos públicos
                        .requestMatchers("/", "/login", "/register", "/css/**", "/js/**", "/images/**", "/api/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/register").permitAll()

                        // ✅ Accesos por rol (según tabla roles en la BD)
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/developer/**").hasAnyRole("DEVELOPER", "ADMIN")
                        .requestMatchers("/user/volverARolUsuario").hasRole("DEVELOPER")
                        .requestMatchers("/user/**").hasAnyRole("USER", "DEVELOPER", "ADMIN")
                        .requestMatchers("/productos/**").hasAnyRole("DEVELOPER", "ADMIN")

                        // ✅ Todo lo demás requiere autenticación
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")                 // página de login (templates/login.html)
                        .loginProcessingUrl("/login")        // endpoint al que envía el formulario
                        .successHandler(loginSuccessHandler) // handler que guarda usuario en sesión
                        .failureUrl("/login?error=true")     // error de login
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .userDetailsService(userDetailsService);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
    return NoOpPasswordEncoder.getInstance();
    }
}

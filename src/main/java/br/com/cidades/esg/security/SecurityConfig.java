package br.com.cidades.esg.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()

                        // Orientações Estratégicas ESG:
                        .requestMatchers(HttpMethod.GET, "/api/orientacoes/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/orientacoes/**").hasRole("LIDER")
                        .requestMatchers(HttpMethod.PUT, "/api/orientacoes/**").hasRole("LIDER")
                        .requestMatchers(HttpMethod.DELETE, "/api/orientacoes/**").hasRole("LIDER")

                        // Ideias de Inovação ESG:
                        .requestMatchers(HttpMethod.POST, "/api/ideias/**").hasAnyRole("OPERADOR", "GESTOR", "LIDER")
                        .requestMatchers(HttpMethod.GET, "/api/ideias/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/ideias/*/status").hasAnyRole("GESTOR", "LIDER")

                        // Projetos ESG:
                        .requestMatchers(HttpMethod.GET, "/api/projetos/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/projetos/**").hasAnyRole("GESTOR", "LIDER")
                        .requestMatchers(HttpMethod.PUT, "/api/projetos/**").hasAnyRole("GESTOR", "LIDER")
                        .requestMatchers(HttpMethod.DELETE, "/api/projetos/**").hasAnyRole("GESTOR", "LIDER")

                        // Dashboard e Chat IA:
                        .requestMatchers("/api/dashboard/**").hasAnyRole("GESTOR", "LIDER")
                        .requestMatchers("/api/ia/**").hasAnyRole("GESTOR", "LIDER")

                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

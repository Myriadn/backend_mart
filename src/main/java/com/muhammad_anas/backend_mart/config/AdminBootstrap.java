package com.muhammad_anas.backend_mart.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.muhammad_anas.backend_mart.user.User;
import com.muhammad_anas.backend_mart.user.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@Component 
@RequiredArgsConstructor 
@ConditionalOnProperty(havingValue = "true", name = "app.bootstrap.admin.enabled")
public class AdminBootstrap implements ApplicationRunner {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppProperties appProperties;

    @Override
    @Transactional 
    public void run(ApplicationArguments args) {
        var cfg = appProperties.getBootstrap().getAdmin();

        if (userRepository.findByUsernameAndDeletedFalse(cfg.getUsername()).isPresent()) {
            log.info("Bootstrap skipped — admin '{}' already exists", cfg.getUsername());
            return;
        }

        User admin = new User();
        admin.setUsername(cfg.getUsername());
        admin.setPassword(passwordEncoder.encode(cfg.getPassword()));
        admin.setRole("ADMIN");
        admin.setActive(true);
        userRepository.save(admin);

        log.info("Bootstrap admin '{}' created", cfg.getUsername());
    }
}

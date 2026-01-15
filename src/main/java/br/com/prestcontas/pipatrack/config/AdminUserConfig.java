package br.com.prestcontas.pipatrack.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import br.com.prestcontas.pipatrack.entities.Role.RoleEnum;
import br.com.prestcontas.pipatrack.entities.User;
import br.com.prestcontas.pipatrack.repositories.RoleRepository;
import br.com.prestcontas.pipatrack.repositories.UserRepository;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private AdminEnvsConfig adminEnvConfig;

    public AdminUserConfig(
        RoleRepository roleRepository, 
        UserRepository userRepository,
        BCryptPasswordEncoder passwordEncoder, 
        AdminEnvsConfig adminEnvConfig
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminEnvConfig = adminEnvConfig;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        var roleAdmin = roleRepository.findByName(RoleEnum.admin);
        var userAdmin = userRepository.findByEmail(adminEnvConfig.getEmail());

         userAdmin.ifPresentOrElse(
                user -> {
                    System.out.println("admin already exists");
                },
                () -> {
                    var user = new User();
                    user.setUsername(adminEnvConfig.getUsername());
                    user.setEmail(adminEnvConfig.getEmail());
                    user.setPassword(passwordEncoder.encode(adminEnvConfig.getPassword()));
                    user.setRole(roleAdmin);
                    userRepository.save(user);
                }
        );
    }

}

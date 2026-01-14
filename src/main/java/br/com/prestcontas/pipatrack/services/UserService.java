package br.com.prestcontas.pipatrack.services;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import br.com.prestcontas.pipatrack.dto.role.RoleItemDTO;
import br.com.prestcontas.pipatrack.dto.town.TownItemDTO;
import br.com.prestcontas.pipatrack.dto.user.LoginRequest;
import br.com.prestcontas.pipatrack.dto.user.LoginResponse;
import br.com.prestcontas.pipatrack.dto.user.RegisterRequestDTO;
import br.com.prestcontas.pipatrack.dto.user.UserUpdateDTO;
import br.com.prestcontas.pipatrack.dto.user.UserResponseDTO;
import br.com.prestcontas.pipatrack.dto.user.UserItemDTO;
import br.com.prestcontas.pipatrack.entities.Role.RoleEnum;
import br.com.prestcontas.pipatrack.entities.Role;
import br.com.prestcontas.pipatrack.entities.Town;
import br.com.prestcontas.pipatrack.entities.User;
import br.com.prestcontas.pipatrack.exception.ForbiddenException;
import br.com.prestcontas.pipatrack.exception.NotFoundException;
import br.com.prestcontas.pipatrack.exception.UnprocessableContentException;
import br.com.prestcontas.pipatrack.repositories.RoleRepository;
import br.com.prestcontas.pipatrack.repositories.TownshipRepository;
import br.com.prestcontas.pipatrack.repositories.UserRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TownshipRepository townshipRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    public UserService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            TownshipRepository townshipRepository,
            BCryptPasswordEncoder passwordEncoder,
            JwtEncoder jwtEncoder) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.townshipRepository = townshipRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    @Transactional
    public void register(RegisterRequestDTO dto) {

        if (userRepository.findByEmail(dto.email()).isPresent() || 
            userRepository.findByUsername(dto.username()).isPresent()) {
            throw new UnprocessableContentException("user already exists");
        }

        var basicRole = roleRepository.findByName(RoleEnum.manager);

        Town town = null;

        if (dto.townshipId() != null) {
            town = townshipRepository.findByTownId(dto.townshipId()).orElseThrow(
                () -> new NotFoundException("Township not found")
            );    
        }

        var user = new User();
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRoles(Set.of(basicRole));
        user.setTown(town);

        userRepository.save(user);
    }

    @Transactional
    public LoginResponse login(LoginRequest loginRequest){
        var user = userRepository.findByEmail(loginRequest.email());

        if (user.isEmpty() || !user.get().isLoginCorrect(loginRequest, passwordEncoder)) {
            throw new BadCredentialsException("user or password invalid");
        }

        var now = Instant.now();
        var expiresIn = 300L;

        var role = user.get().getRoles()
            .stream()
            .map(Role::getName)
            .map(RoleEnum::name)
            .collect(Collectors.joining(" "));
        
        
        var scopes = String.join(" ", role);


        var claims = JwtClaimsSet.builder()
            .issuer("nergal.com")
            .subject(user.get().getUserId().toString())
            .expiresAt(now.plusSeconds(expiresIn))
            .claim("scope", scopes)
            .issuedAt(now)
            .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new LoginResponse(jwtValue, expiresIn);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO listUsers(int page, int pageSize){
        var users = userRepository.findAll(PageRequest.of(page, pageSize, Sort.Direction.ASC, "username"))   
            .map(user -> 
                new UserItemDTO(
                    user.getUserId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRoles()
                        .stream()
                        .map(role -> new RoleItemDTO(
                            role.getRoleId(), 
                            role.getName()
                        )).collect(Collectors.toList()),
                    user.getTown() != null ? new TownItemDTO(
                        user.getTown().getTownId(),
                        user.getTown().getName(),
                        user.getTown().getUf(),
                        user.getTown().getImageUrl(),
                        user.getTown().getCnpj(),
                        user.getTown().getCode()
                    ) : null,
                    user.getCreatedAt()
                ));

        return new UserResponseDTO(
            users.getContent(),
            page,
            pageSize,
            users.getTotalPages(),
            users.getTotalElements()
        );
    }

    protected void applyUpdates(User entity, UserUpdateDTO dto) {
        if (dto.username() != null) {
            entity.setUsername(dto.username());
        }
        if (dto.email() != null) {
            entity.setEmail(dto.email());
        }
        if (dto.role() != null) {
            var role = roleRepository.findByName(dto.role());
            if (role == null) {
                throw new NotFoundException("Role not found");
            }
            entity.setRoles(Set.of(role));
        }
        if (dto.password() != null && !dto.password().isEmpty()) {
            entity.setPassword(passwordEncoder.encode(dto.password()));
        }
        if (dto.townshipId() != null) {
            var township = townshipRepository.findByTownId(dto.townshipId())
                .orElseThrow(() -> new NotFoundException("Township not found"));
            entity.setTown(township);
        }
    }

    @Transactional
    public void updateUser(UUID userId, UserUpdateDTO dto){
        var user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found"));

        applyUpdates(user, dto);

        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(UUID userId, JwtAuthenticationToken token){
        var user = userRepository.findById(UUID.fromString(token.getName()));

        var isAdmin = user.get().getRoles()
            .stream()
            .anyMatch(
                role -> role.getName().name().equalsIgnoreCase(
                    RoleEnum.admin.name()
                )
            );

        var userToDelete = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found"));

        if (isAdmin || userToDelete.getUserId().equals(UUID.fromString(token.getName()))) {
            userRepository.deleteById(userId);
        } else {
            throw new ForbiddenException("You do not have permission to delete this user.");
        }
    }
}

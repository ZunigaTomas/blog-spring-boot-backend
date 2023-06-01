package com.springBoot.blog.controlador;

import com.springBoot.blog.dto.JWTAuthResponseDTO;
import com.springBoot.blog.dto.LoginDTO;
import com.springBoot.blog.dto.RegistroDTO;
import com.springBoot.blog.entidades.Rol;
import com.springBoot.blog.entidades.Usuario;
import com.springBoot.blog.repositorio.RolRepositorio;
import com.springBoot.blog.repositorio.UsuarioRepositorio;
import com.springBoot.blog.seguridad.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth/")
public class AuthControlador {
    private final AuthenticationManager authenticationManager;
    private final UsuarioRepositorio usuarioRepositorio;
    private final RolRepositorio rolRepositorio;
    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;
    @Autowired
    public AuthControlador(AuthenticationManager authenticationManager, UsuarioRepositorio usuarioRepositorio,
                           RolRepositorio rolRepositorio, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepositorio = usuarioRepositorio;
        this.rolRepositorio = rolRepositorio;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    @PostMapping("/iniciarSesion")
    public ResponseEntity<JWTAuthResponseDTO> autenticarUsuario(@RequestBody LoginDTO loginDTO) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsernameOrEmail(),loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generarToken(authentication);

        return ResponseEntity.ok(new JWTAuthResponseDTO(token));
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarUsuario(@RequestBody RegistroDTO registroDTO) {
        if(usuarioRepositorio.existsByUsername(registroDTO.getUsername())) {
            return new ResponseEntity<>("Ese nombre de usuario ya esta en uso.", HttpStatus.BAD_REQUEST);
        }
        if(usuarioRepositorio.existsByEmail(registroDTO.getEmail())) {
            return new ResponseEntity<>("Ese email ya esta en uso.", HttpStatus.BAD_REQUEST);
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(registroDTO.getNombre());
        usuario.setUsername(registroDTO.getUsername());
        usuario.setEmail(registroDTO.getEmail());
        usuario.setPassword(passwordEncoder.encode(registroDTO.getPassword()));

        Rol roles = rolRepositorio.findByNombre("ROLE_ADMIN").get();
        usuario.setRoles(Collections.singleton(roles));

        usuarioRepositorio.save(usuario);

        return new ResponseEntity<>("Usuario registrado correctamente", HttpStatus.OK);
    }
}

package com.telefonicatech.cmdbChile.service.usuarios;

import com.telefonicatech.cmdbChile.dto.requestObject.LoginRequest;
import com.telefonicatech.cmdbChile.dto.responseObject.UsuarioResponse;
import com.telefonicatech.cmdbChile.helper.PasswordUtil;
import com.telefonicatech.cmdbChile.mapper.UsuarioMapper;
import com.telefonicatech.cmdbChile.model.usuarios.Usuario;
import com.telefonicatech.cmdbChile.repository.usuarios.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper mapper;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper mapper) {
        this.usuarioRepository = usuarioRepository;
        this.mapper = mapper;
    }

    public UsuarioResponse autenticar(LoginRequest login){
        Usuario usr = usuarioRepository.getReferenceById(login.getIdUsuario());
        boolean ok= PasswordUtil.matches(login.getClave(), usr.getClaveUsuario());

        if (!ok) {
            throw new RuntimeException("Password incorrecta");
        }

        return mapper.toResponse(usr);
    }
}

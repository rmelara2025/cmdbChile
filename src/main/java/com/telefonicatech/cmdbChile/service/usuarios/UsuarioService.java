package com.telefonicatech.cmdbChile.service.usuarios;

import com.telefonicatech.cmdbChile.dto.requestObject.LoginRequest;
import com.telefonicatech.cmdbChile.dto.responseObject.UsuarioResponse;
import com.telefonicatech.cmdbChile.dto.responseObject.UsuarioRolResponse;
import com.telefonicatech.cmdbChile.helper.PasswordUtil;
import com.telefonicatech.cmdbChile.mapper.UsuarioMapper;
import com.telefonicatech.cmdbChile.model.usuarios.Usuario;
import com.telefonicatech.cmdbChile.repository.usuarios.RolRepository;
import com.telefonicatech.cmdbChile.repository.usuarios.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioMapper mapper;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper mapper, RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.mapper = mapper;
        this.rolRepository = rolRepository;
    }

    public UsuarioResponse autenticar(LoginRequest login){
        Usuario usr = usuarioRepository.getReferenceById(login.getIdUsuario());
        boolean ok= PasswordUtil.matches(login.getClave(), usr.getClaveUsuario());

        if (!ok) {
            throw new RuntimeException("Password incorrecta");
        }

        return mapper.toResponse(usr);
    }

    public List<UsuarioRolResponse> obtenerRoles(String idusuario){
        return rolRepository.findRolByUserId(idusuario);
    }
}

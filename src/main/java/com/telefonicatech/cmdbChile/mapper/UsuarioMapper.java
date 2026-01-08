package com.telefonicatech.cmdbChile.mapper;

import com.telefonicatech.cmdbChile.dto.responseObject.UsuarioResponse;
import com.telefonicatech.cmdbChile.model.usuarios.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UsuarioResponse toResponse(Usuario usr){
        return new UsuarioResponse() {
            {
                setNombreUsuario(usr.getNombreUsuario());
                setEmail(usr.getEmailUsuario());
            }
        };
    }
}

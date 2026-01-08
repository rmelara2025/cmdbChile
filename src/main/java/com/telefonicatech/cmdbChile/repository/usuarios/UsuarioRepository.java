package com.telefonicatech.cmdbChile.repository.usuarios;

import com.telefonicatech.cmdbChile.model.usuarios.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {

}

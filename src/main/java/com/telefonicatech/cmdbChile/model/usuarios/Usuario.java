package com.telefonicatech.cmdbChile.model.usuarios;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "usuario")
public class Usuario {

    @Id
    @Column(name = "idusuario", length = 20, nullable = false)
    private String idUsuario;

    @Column(name = "nombreUsuario", length = 100)
    private String nombreUsuario;

    @Column(name = "claveUsuario", length = 100, nullable = false)
    private String claveUsuario;

    @Column(name = "emailUsuario", length = 200)
    private String emailUsuario;
}

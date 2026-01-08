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
@Entity(name = "rol")
public class Rol {

    @Id
    @Column(name = "idRol", nullable = false)
    private String idRol;

    @Column(name = "nombreRol", length = 45)
    private String nombreRol;

    @Column(name = "descripcionRol")
    private String descripcionRol;

}

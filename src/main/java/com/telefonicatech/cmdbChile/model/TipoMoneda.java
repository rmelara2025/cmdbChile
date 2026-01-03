package com.telefonicatech.cmdbChile.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tipomoneda")
public class TipoMoneda {

    @Id
    @Column(name = "idTipoMoneda")
    private Integer idTipoMoneda;

    @Column(name = "nombreTipoMoneda")
    private String nombreTipoMoneda;
}

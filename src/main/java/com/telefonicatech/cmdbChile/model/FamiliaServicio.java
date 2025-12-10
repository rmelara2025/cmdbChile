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
@Entity(name = "familiaservicio")
public class FamiliaServicio {

    @Id
    @Column(name = "idFamilia")
    private Integer idFamilia;

    @Column(name = "nombreFamilia")
    private String nombreFamilia;

    @Column(name = "descripcion")
    private String descripcion;
}


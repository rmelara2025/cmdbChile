package com.telefonicatech.cmdbChile.model.usuarios;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transicionestado")
public class TransicionEstado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTransicion")
    private Integer idTransicion;

    @Column(name = "idEstadoOrigen")
    private Integer idEstadoOrigen;

    @Column(name = "idEstadoDestino", nullable = false)
    private Integer idEstadoDestino;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "requiereComentario", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean requiereComentario;

    @Column(name = "requiereMotivoRechazo", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean requiereMotivoRechazo;

    @Column(name = "activo", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean activo;

    @Column(name = "fechaCreacion", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fechaCreacion;
}

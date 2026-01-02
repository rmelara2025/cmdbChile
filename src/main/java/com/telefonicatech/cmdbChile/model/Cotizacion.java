package com.telefonicatech.cmdbChile.model;

import com.telefonicatech.cmdbChile.mapper.UUIDBinaryConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cotizacion")
public class Cotizacion {

    @Id
    @Column(name = "idCotizacion", columnDefinition = "BINARY(16)")
    @Convert(converter = UUIDBinaryConverter.class)
    private UUID idCotizacion;

    @Column(name = "idContrato", columnDefinition = "BINARY(16)", nullable = false)
    @Convert(converter = UUIDBinaryConverter.class)
    private UUID idContrato;

    @Column(name = "numeroCotizacion", nullable = false, length = 45)
    private String numeroCotizacion;

    @Column(name = "version", nullable = false)
    private Integer version;

    @Column(name = "idEstadoCotizacion", nullable = false)
    private Integer idEstadoCotizacion;

    @Column(name = "fechaEmision", nullable = false)
    private LocalDate fechaEmision;

    @Column(name = "fechaVigenciaDesde")
    private LocalDate fechaVigenciaDesde;

    @Column(name = "fechaVigenciaHasta")
    private LocalDate fechaVigenciaHasta;

    @Column(name = "observacion", length = 255)
    private String observacion;

    @Column(name = "idUsuarioCreacion", length = 20)
    private String idUsuarioCreacion;

    @Column(name = "fechaRegistro")
    private LocalDateTime fechaRegistro;

    @Column(name = "fechaModificacion")
    private LocalDateTime fechaModificacion;
}

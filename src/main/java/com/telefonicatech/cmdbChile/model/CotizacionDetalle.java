package com.telefonicatech.cmdbChile.model;

import com.telefonicatech.cmdbChile.mapper.UUIDBinaryConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CotizacionDetalle {
    @Id
    @Column(name = "idDetalle", columnDefinition = "BINARY(16)")
    @Convert(converter = UUIDBinaryConverter.class)
    private UUID idDetalle;

    @Column(name = "num_item")
    private Integer numItem;

    @Column(name = "version_cotizacion")
    private Integer versionCotizacion;

    @Column(name = "idContrato", columnDefinition = "BINARY(16)")
    @Convert(converter = UUIDBinaryConverter.class)
    private UUID idContrato;

    @Column(name = "idServicio")
    private Integer idServicio;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "recurrente")
    private BigDecimal recurrente;

    @Column(name = "atributos", columnDefinition = "json")
    private String atributos; // o Map<String,Object> con @Type(JsonType)

    @Column(name = "fechaRegistro")
    private LocalDateTime fechaRegistro;

}

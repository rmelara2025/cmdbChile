package com.telefonicatech.cmdbChile.model;

import com.telefonicatech.cmdbChile.mapper.UUIDBinaryConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contrato")
public class Contrato {

    @Id
    @Column(name = "idContrato", columnDefinition = "BINARY(16)")
    @Convert(converter = UUIDBinaryConverter.class)
    private UUID idContrato;

    @Column(name = "rutCliente", nullable = false, length = 12)
    private String rutCliente;

    @Column(name = "cod_sison", length = 50)
    private String codSison;

    @Column(name = "cod_chi", length = 50)
    private String codChi;

    @Column(name = "cod_sap", length = 50)
    private String codSap;

    @Column(name = "fechaInicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fechaTermino", nullable = false)
    private LocalDate fechaTermino;

    @Column(name = "observacion", length = 255)
    private String observacion;

    @PrePersist
    public void generarId() {
        if (this.idContrato == null) {
            this.idContrato = UUID.randomUUID();
        }
    }
}

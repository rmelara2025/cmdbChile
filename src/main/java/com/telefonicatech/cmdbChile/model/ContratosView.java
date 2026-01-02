package com.telefonicatech.cmdbChile.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@Setter
@Entity
@Immutable
@Table(name="contratosview")
@ToString
public class ContratosView {
    @Id
    @Column( name = "idContrato")
    private String idContrato;

    @Column(name = "rutCliente")
    private String rutCliente;

    @Column(name = "nombreCliente")
    private String nombreCliente;
    @Column(name = "fechaInicio")
    private LocalDate fechaInicio;
    @Column(name = "fechaTermino")
    private LocalDate fechaTermino;
    @Column(name = "codChi")
    private String codChi;
    @Column(name = "codSap")
    private String codSap;
    @Column(name = "codSison")
    private String codSison;

}

package com.telefonicatech.cmdbChile.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Immutable;


import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Immutable
@Table(name = "dashboardtotalrecurrentesview")
@ToString
public class DashboardRecurrentesView {
    @Id
    @Column(name = "id")  // Debes crear este alias en la vista
    private Integer id;

    private String nombreTipoMoneda;
    private String estado;
    private BigDecimal totalRecurrente;
    private Integer cantidadContratos;

}

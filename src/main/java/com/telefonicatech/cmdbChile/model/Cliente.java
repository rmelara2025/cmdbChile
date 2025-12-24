package com.telefonicatech.cmdbChile.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;
import com.telefonicatech.cmdbChile.helper.RutUtils;

@Data
@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @Column(name = "rutCliente", length = 12)
    private String rutCliente;

    @Column(name = "nombreCliente", length = 255)
    private String nombreCliente;

    @Column(name = "nombreComercial", length = 255)
    private String nombreComercial;

    @Column(name = "razonSocial", length = 255)
    private String razonSocial;

    @Column(name = "estado")
    private Integer estado;

    // Normalize and validate RUT before saving/updating
    @PrePersist
    @PreUpdate
    public void normalizeRut() {
        if (this.rutCliente != null) {
            String formatted = RutUtils.formatRut(this.rutCliente);
            if (!RutUtils.validateRut(formatted)) {
                throw new IllegalArgumentException("RUT inválido: " + this.rutCliente);
            }
            this.rutCliente = formatted;
        }
    }
}
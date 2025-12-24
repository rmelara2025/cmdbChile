package com.telefonicatech.cmdbChile.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;
import com.telefonicatech.cmdbChile.helper.RutUtils;

@Data
@Entity
@Table(name = "contacto")
public class Contacto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcontacto")
    private Integer idcontacto;

    @Column(name = "rutCliente", length = 12, nullable = false)
    private String rutCliente;

    @Column(name = "telefono", length = 45)
    private String telefono;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "cargo", length = 100)
    private String cargo;

    @PrePersist
    @PreUpdate
    public void normalizeRut() {
        if (this.rutCliente != null) {
            String formatted = RutUtils.formatRut(this.rutCliente);
            if (!RutUtils.validateRut(formatted)) {
                throw new IllegalArgumentException("RUT inválido en contacto: " + this.rutCliente);
            }
            this.rutCliente = formatted;
        }
    }
}

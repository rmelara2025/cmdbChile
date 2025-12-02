package com.telefonicatech.cmdbChile.model;

import com.telefonicatech.cmdbChile.dto.ContratoFilterRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class ContratoViewSpecs {
    public static Specification<ContratosView> withFilters(ContratoFilterRequest filter) {
        return Specification.where(rut(filter.getRutCliente()))
                .and(nombreCliente(filter.getNombreCliente()))
                .and(codChi(filter.getCodChi()))
                .and(codSap(filter.getCodSap()))
                .and(codSison(filter.getCodSison()))
                .and(estado(filter.getEstado()));
    }

    private static Specification<ContratosView> rut(String rut) {
        return (root, query, cb) ->
                rut == null || rut.isBlank()
                        ? null
                        : cb.equal(root.get("rutCliente"), rut);
    }

    private static Specification<ContratosView> nombreCliente(String nombre) {
        return (root, query, cb) ->
                nombre == null || nombre.isBlank()
                        ? null
                        : cb.like(cb.lower(root.get("nombreCliente")), "%" + nombre.toLowerCase() + "%");
    }

    private static Specification<ContratosView> codChi(String cod) {
        return (root, query, cb) ->
                cod == null || cod.isBlank()
                        ? null
                        : cb.equal(root.get("codChi"), cod);
    }

    private static Specification<ContratosView> codSap(String cod) {
        return (root, query, cb) ->
                cod == null || cod.isBlank()
                        ? null
                        : cb.equal(root.get("codSap"), cod);
    }

    private static Specification<ContratosView> codSison(String cod) {
        return (root, query, cb) ->
                cod == null || cod.isBlank()
                        ? null
                        : cb.equal(root.get("codSison"), cod);
    }

    public static Specification<ContratosView> estado(String estado) {
        return (root, query, cb) -> {
            if (estado == null || estado.isBlank()) {
                return cb.conjunction(); // sin filtro
            }

            LocalDate hoy = LocalDate.now();

            switch (estado) {
                case "expirado":
                    return cb.lessThan(root.get("fechaTermino"), hoy);

                case "por-expirar":
                    return cb.between(root.get("fechaTermino"), hoy, hoy.plusDays(90));

                case "vigente":
                    return cb.greaterThan(root.get("fechaTermino"), hoy.plusDays(90));

                default:
                    return cb.conjunction();
            }
        };
    }

}

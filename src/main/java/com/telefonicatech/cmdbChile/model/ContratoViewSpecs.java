package com.telefonicatech.cmdbChile.model;

import com.telefonicatech.cmdbChile.dto.ContratoFilterRequest;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class ContratoViewSpecs {


    public static Specification<ContratosView> withFilters(ContratoFilterRequest filter) {
//        return Specification.where(rut(filter.getRutCliente()))
//                .and(nombreCliente(filter.getNombreCliente()))
//                .and(codChi(filter.getCodChi()))
//                .and(codSap(filter.getCodSap()))
//                .and(codSison(filter.getCodSison()))
//                .and(estado(filter.getEstado()));

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // FILTRO: RUT
            if (filter.getRutCliente() != null && !filter.getRutCliente().isBlank()) {
                predicates.add(cb.equal(root.get("rutCliente"), filter.getRutCliente()));
            }

            // FILTRO: codSap
            if (filter.getCodSap() != null && !filter.getCodSap().isBlank()) {
                predicates.add(cb.equal(root.get("codSap"), filter.getCodSap()));
            }

            // FILTRO: codChi
            if (filter.getCodChi() != null && !filter.getCodChi().isBlank()) {
                predicates.add(cb.equal(root.get("codChi"), filter.getCodChi()));
            }

            // FILTRO: codSison
            if (filter.getCodSison() != null && !filter.getCodSison().isBlank()) {
                predicates.add(cb.equal(root.get("codSison"), filter.getCodSison()));
            }

            // FILTRO: nombreCliente contiene
            if (filter.getNombreCliente() != null && !filter.getNombreCliente().isBlank()) {
                predicates.add(
                        cb.like(cb.lower(root.get("nombreCliente")), "%" + filter.getNombreCliente().toLowerCase() + "%")
                );
            }

            // FILTRO: ESTADO
            if (filter.getEstado() != null && !filter.getEstado().equals("todos")) {

                LocalDate hoy = LocalDate.now();
                Expression<LocalDate> fechaTermino = root.get("fechaTermino");

                switch (filter.getEstado()) {
                    case "expirado":
                        predicates.add(cb.lessThan(fechaTermino, hoy));
                        break;

                    case "por-expirar":
                        predicates.add(cb.between(fechaTermino, hoy, hoy.plusDays(90)));
                        break;

                    case "vigente":
                        predicates.add(cb.greaterThan(fechaTermino, hoy.plusDays(90)));
                        break;
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };


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
                case "todos":
                default:
                    return cb.conjunction();
            }
        };
    }

}

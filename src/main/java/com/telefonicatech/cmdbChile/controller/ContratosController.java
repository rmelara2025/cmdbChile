package com.telefonicatech.cmdbChile.controller;

import com.telefonicatech.cmdbChile.dto.requestObject.ContratoFilterRequest;
import com.telefonicatech.cmdbChile.dto.responseObject.ContratoViewResponse;
import com.telefonicatech.cmdbChile.helper.HelperCommons;
import com.telefonicatech.cmdbChile.service.ContratoViewService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.List;



@RestController
@RequestMapping(value = "/api/contratos", produces = MediaType.APPLICATION_JSON_VALUE)
public class ContratosController {

    private final ContratoViewService service;
    private final HelperCommons helpers;

    public ContratosController(ContratoViewService service, HelperCommons helpers) {
        this.service = service;
        this.helpers = helpers;
    }

    @GetMapping
    public ResponseEntity<Page<ContratoViewResponse>> listar(@ModelAttribute ContratoFilterRequest filter) {

        List<Sort.Order> orders = helpers.parseSort(filter.getSort());


        Pageable pageable = PageRequest.of(
                filter.getPage(),
                filter.getSize(),
                Sort.by(orders)
        );

        Page<ContratoViewResponse> page = service.listar(filter, pageable);
        return ResponseEntity.ok(page);
    }

}

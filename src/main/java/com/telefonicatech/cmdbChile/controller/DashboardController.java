package com.telefonicatech.cmdbChile.controller;

import com.telefonicatech.cmdbChile.dto.TotalRecurrenteResponse;
import com.telefonicatech.cmdbChile.service.TotalRecurrentesService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/dashboard", produces = MediaType.APPLICATION_JSON_VALUE)
public class DashboardController {

    private final TotalRecurrentesService service;

    public DashboardController(TotalRecurrentesService service){
        this.service = service;
    }

    @GetMapping("/recurrentes")
    public List<TotalRecurrenteResponse> getData() {
        return service.getData();
    }
}

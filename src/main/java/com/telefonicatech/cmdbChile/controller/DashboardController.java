package com.telefonicatech.cmdbChile.controller;

import com.telefonicatech.cmdbChile.dto.RecurrenteRequest;
import com.telefonicatech.cmdbChile.dto.RecurrenteResponse;
import com.telefonicatech.cmdbChile.service.RecurrentesService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/dashboard", produces = MediaType.APPLICATION_JSON_VALUE)
public class DashboardController {

    private final RecurrentesService service;

    public DashboardController(RecurrentesService service){
        this.service = service;
    }

    @GetMapping("/recurrentes")
    public ResponseEntity<List<RecurrenteResponse>> getData() {
        List<RecurrenteResponse> res = service.getData();
        return ResponseEntity.ok(res);
    }


    @PostMapping("/custom")
    public ResponseEntity<List<RecurrenteResponse>> search(@RequestBody(required = false) RecurrenteRequest req) {
        if (req == null) req = new RecurrenteRequest();
        List<RecurrenteResponse> results = service.getCustomData(req);
        return ResponseEntity.ok(results);
    }
}

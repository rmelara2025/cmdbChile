package com.telefonicatech.cmdbChile.service;

import com.telefonicatech.cmdbChile.dto.requestObject.ClienteRequest;
import com.telefonicatech.cmdbChile.dto.responseObject.ClienteResponse;
import com.telefonicatech.cmdbChile.mapper.ClienteMapper;
import com.telefonicatech.cmdbChile.model.Cliente;
import com.telefonicatech.cmdbChile.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    ClienteRepository repository;

    @Mock
    ClienteMapper mapper;

    @InjectMocks
    ClienteService service;

    private ClienteRequest req;

    @BeforeEach
    void setUp() {
        req = new ClienteRequest();
        req.setRutCliente("99598680-9");
        req.setNombreCliente("Pepito SA");
    }

    @Test
    void create_happyPath() {
        Cliente entity = new Cliente();
        entity.setRutCliente(req.getRutCliente());
        entity.setNombreCliente(req.getNombreCliente());
        when(repository.existsById(req.getRutCliente())).thenReturn(false);
        when(mapper.toEntityForCreate(req)).thenReturn(entity);
        when(repository.save(org.mockito.ArgumentMatchers.any(Cliente.class))).thenReturn(entity);
        
        ClienteResponse response = new ClienteResponse();
        response.setRutCliente(entity.getRutCliente());
        response.setNombreCliente(entity.getNombreCliente());
        when(mapper.toResponse(entity)).thenReturn(response);

        ClienteResponse res = service.create(req);
        assertThat(res).isNotNull();
        assertThat(res.getRutCliente()).isEqualTo(req.getRutCliente());
    }

    @Test
    void create_whenExists_throws() {
        when(repository.existsById(req.getRutCliente())).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> service.create(req));
    }
}


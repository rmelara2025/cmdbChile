package com.telefonicatech.cmdbChile.service;

import com.telefonicatech.cmdbChile.dto.requestObject.ContactoRequest;
import com.telefonicatech.cmdbChile.dto.responseObject.ContactoResponse;
import com.telefonicatech.cmdbChile.exception.NotFoundException;
import com.telefonicatech.cmdbChile.mapper.ContactoMapper;
import com.telefonicatech.cmdbChile.model.Contacto;
import com.telefonicatech.cmdbChile.repository.ClienteRepository;
import com.telefonicatech.cmdbChile.repository.ContactoRepository;
import com.telefonicatech.cmdbChile.service.impl.ContactoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContactoServiceImplTest {

    @Mock
    ContactoRepository contactoRepository;

    @Mock
    ContactoMapper contactoMapper;

    @Mock
    ClienteRepository clienteRepository;

    @InjectMocks
    ContactoServiceImpl service;

    @Captor
    ArgumentCaptor<Contacto> contactoCaptor;

    private ContactoRequest validRequest;
    private Contacto entity;

    @BeforeEach
    void setUp() {
        validRequest = new ContactoRequest();
        validRequest.setRutCliente("99598680-9");
        validRequest.setNombre("Pepito");
        validRequest.setEmail("pepito@example.com");
        validRequest.setTelefono("+56912345678");

        entity = new Contacto();
        entity.setRutCliente("99598680-9");
        entity.setNombre("Pepito");
        entity.setEmail("pepito@example.com");
        entity.setTelefono("+56912345678");
    }

    @Test
    void create_whenClientExists_savesAndReturnsResponse() {
        // arrange
        when(clienteRepository.existsById("99598680-9")).thenReturn(true);
        when(contactoMapper.toEntity(any())).thenReturn(entity);
        Contacto saved = new Contacto();
        saved.setRutCliente(entity.getRutCliente());
        saved.setNombre(entity.getNombre());
        when(contactoRepository.save(any())).thenReturn(saved);
        ContactoResponse resp = new ContactoResponse(1, saved.getRutCliente(), saved.getTelefono(), saved.getNombre(), saved.getEmail(), null);
        when(contactoMapper.toResponse(saved)).thenReturn(resp);

        // act
        ContactoResponse result = service.create(validRequest);

        // assert
        verify(clienteRepository).existsById("99598680-9");
        verify(contactoMapper).toEntity(validRequest);
        verify(contactoRepository).save(contactoCaptor.capture());
        verify(contactoMapper).toResponse(saved);

        Contacto captured = contactoCaptor.getValue();
        assertThat(captured.getRutCliente()).isEqualTo("99598680-9");
        assertThat(result).isEqualTo(resp);
    }

    @Test
    void create_whenClientNotFound_throwsNotFound() {
        when(clienteRepository.existsById("99598680-9")).thenReturn(false);

        assertThrows(NotFoundException.class, () -> service.create(validRequest));

        verify(contactoRepository, never()).save(any());
    }

    @Test
    void getById_whenExists_returnsResponse() {
        Contacto c = new Contacto();
        c.setIdcontacto(5);
        c.setRutCliente("99598680-9");
        when(contactoRepository.findById(5)).thenReturn(Optional.of(c));
        ContactoResponse resp = new ContactoResponse(5, c.getRutCliente(), null, null, null, null);
        when(contactoMapper.toResponse(c)).thenReturn(resp);

        ContactoResponse r = service.getById(5);
        assertThat(r).isEqualTo(resp);
    }

    @Test
    void delete_whenNotExists_throwsNotFound() {
        when(contactoRepository.existsById(10)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> service.delete(10));
    }
}


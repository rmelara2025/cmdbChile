package com.telefonicatech.cmdbChile.service.externos;

import com.telefonicatech.cmdbChile.config.CmfClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class IndicadoresService {
    private final CmfClient cmfClient;

    public IndicadoresService(CmfClient cmfClient) {
        this.cmfClient = cmfClient;
    }

    @Cacheable(value = "uf", unless = "#result == null")
    public String obtenerUf() {
        return cmfClient.getUfRaw();
    }

    @Cacheable(value = "dolar", unless = "#result == null")
    public String obtenerDolar() {
        return cmfClient.getDolarRaw();
    }
}

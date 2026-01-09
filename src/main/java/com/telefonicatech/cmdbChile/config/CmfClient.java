package com.telefonicatech.cmdbChile.config;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class CmfClient {
    private final WebClient webClient;
    private final CmfConfig props;

    public CmfClient(WebClient.Builder builder, CmfConfig props) {
        this.props = props;
        this.webClient = builder.baseUrl(props.getBaseUrl()).build();
    }

    public String getUfRaw() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/uf")
                        .queryParam("apikey", props.getKey())
                        .queryParam("formato", props.getFormato())
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String getDolarRaw() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/dolar")
                        .queryParam("apikey", props.getKey())
                        .queryParam("formato", props.getFormato())
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}

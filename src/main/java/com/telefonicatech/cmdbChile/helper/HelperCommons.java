package com.telefonicatech.cmdbChile.helper;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
@Component
public class HelperCommons {

    public List<Sort.Order> parseSort(List<String> sortParams) {

        // 1. Limpiar valores y separarlos por coma cuando aplique
        List<String> tokens = sortParams.stream()
                .map(s -> s.replaceAll("[\\[\\]\"]", "").trim())
                .flatMap(s -> Arrays.stream(s.split(",")))
                .filter(t -> !t.isBlank())
                .toList();

        // 2. Reconstruir pares campo + dirección
        List<String> normalized = new ArrayList<>();
        for (int i = 0; i < tokens.size();) {
            String field = tokens.get(i);

            if (i + 1 < tokens.size() &&
                    (tokens.get(i + 1).equalsIgnoreCase("asc")
                            || tokens.get(i + 1).equalsIgnoreCase("desc"))) {

                normalized.add(field + "," + tokens.get(i + 1));
                i += 2;
            } else {
                normalized.add(field);
                i++;
            }
        }

        // 3. Mapeo limpio a Sort.Order
        return normalized.stream()
                .map(item -> {
                    String[] parts = item.split(",");
                    String field = parts[0];
                    Sort.Direction dir = (parts.length == 2)
                            ? Sort.Direction.fromString(parts[1])
                            : Sort.Direction.ASC;
                    return new Sort.Order(dir, field);
                })
                .toList();
    }

}

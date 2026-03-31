package com.progetto.ingsw.backend.mapper;

import com.progetto.ingsw.backend.dto.AutoDTO;
import com.progetto.ingsw.backend.model.Auto;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class AutoMapper {

    public AutoDTO mapToDTO(Auto auto) {
        if (auto == null) return null;
        AutoDTO dto = new AutoDTO();

        // Dati Base
        dto.setId(auto.getId());
        dto.setMarca(auto.getMarca());
        dto.setModello(auto.getModello());
        dto.setAnno(auto.getAnno());
        dto.setPrezzo(auto.getPrezzo());
        dto.setCategoria(auto.getCategoria());
        dto.setCodice(auto.getCodice());

        // Parametri Prestazionali (con gestione null per sicurezza)
        dto.setCv(auto.getCv());
        dto.setVelocitaMax(auto.getVelocitaMax());
        dto.setCoppia(auto.getCoppia());
        dto.setCilindri(auto.getCilindri());
        dto.setCilindrata(auto.getCilindrata());
        dto.setPesoKg(auto.getPesoKg());
        dto.setTrazione(auto.getTrazione());
        dto.setAlimentazione(auto.getAlimentazione());
        dto.setEuro(auto.getEuro());
        dto.setCo2(auto.getCo2());

        // Conversione Double -> BigDecimal
        dto.setAccelerazione0100(auto.getAccelerazione0100() != null ? BigDecimal.valueOf(auto.getAccelerazione0100()) : null);
        dto.setAccelerazione100200(auto.getAccelerazione100200() != null ? BigDecimal.valueOf(auto.getAccelerazione100200()) : null);
        dto.setAccelerazione80140(auto.getAccelerazione80140() != null ? BigDecimal.valueOf(auto.getAccelerazione80140()) : null);
        dto.setRapportoPesoPotenza(auto.getRapportoPesoPotenza() != null ? BigDecimal.valueOf(auto.getRapportoPesoPotenza()) : null);
        dto.setConsumoMedio(auto.getConsumoMedio() != null ? BigDecimal.valueOf(auto.getConsumoMedio()) : null);

        return dto;
    }
}

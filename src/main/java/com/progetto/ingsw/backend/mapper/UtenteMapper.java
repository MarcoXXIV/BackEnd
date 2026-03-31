package com.progetto.ingsw.backend.mapper;

import com.progetto.ingsw.backend.dto.UtenteDTO;
import com.progetto.ingsw.backend.model.Utente;
import org.springframework.stereotype.Component;

@Component
public class UtenteMapper {

    public UtenteDTO mapToDTO(Utente u) {
        if (u == null) return null;

        UtenteDTO dto = new UtenteDTO();
        dto.setId(u.getId());
        dto.setNome(u.getNome());
        dto.setCognome(u.getCognome());
        dto.setEmail(u.getEmail());
        dto.setRuolo(u.getRuolo());

        return dto;
    }
}
package com.progetto.ingsw.backend.mapper;

import com.progetto.ingsw.backend.dto.PreferitoDTO;
import com.progetto.ingsw.backend.model.Preferiti;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PreferitiMapper {

    @Autowired
    private ConfrontoMapper confrontoMapper;

    @Autowired
    private UtenteMapper utenteMapper;

    public PreferitoDTO mapToDTO(Preferiti p) {
        if (p == null) return null;

        PreferitoDTO dto = new PreferitoDTO();
        dto.setId(p.getId());
        dto.setDataAggiunta(p.getDataAggiunta());

        // 1. Mappiamo l'utente proprietario della lista preferiti
        if (p.getUtente() != null) {
            dto.setUtente(utenteMapper.mapToDTO(p.getUtente()));
        }

        // 2. Mappiamo il confronto salvato (che include auto e parametri)
        if (p.getConfronto() != null) {
            // Deleghiamo al mapper specializzato
            dto.setConfronto(confrontoMapper.mapToDTO(p.getConfronto()));
        }

        return dto;
    }
}
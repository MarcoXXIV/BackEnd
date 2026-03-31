package com.progetto.ingsw.backend.mapper;

import com.progetto.ingsw.backend.dto.RecensioneDTO;
import com.progetto.ingsw.backend.model.Recensioni;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RecensioniMapper {

    @Autowired
    private UtenteMapper utenteMapper;

    @Autowired
    private ConfrontoMapper confrontoMapper;

    public RecensioneDTO mapToDTO(Recensioni r) {
        if (r == null) return null;

        RecensioneDTO dto = new RecensioneDTO();
        dto.setId(r.getId());
        dto.setVoto(r.getVoto());
        dto.setCommento(r.getCommento());

        // Usiamo la data effettiva della recensione
        if (r.getDataRecensione() != null) {
            dto.setData(r.getDataRecensione().toString());
        }

        // 1. Mappatura Utente (Chi ha scritto la recensione)
        if (r.getUtente() != null) {
            dto.setUtente(utenteMapper.mapToDTO(r.getUtente()));
        }

        // 2. Mappatura Confronto (A quale sfida si riferisce)
        if (r.getConfronto() != null) {
            dto.setConfronto(confrontoMapper.mapToDTO(r.getConfronto()));
        }

        return dto;
    }
}
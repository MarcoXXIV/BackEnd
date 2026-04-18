package com.progetto.ingsw.backend.mapper;

import com.progetto.ingsw.backend.dto.ConfrontoDTO;
import com.progetto.ingsw.backend.model.Confronto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConfrontoMapper {

    @Autowired
    private AutoMapper autoMapper;

    @Autowired
    private UtenteMapper utenteMapper;

    public ConfrontoDTO mapToDTO(Confronto c) {
        if (c == null) return null;

        ConfrontoDTO dto = new ConfrontoDTO();
        dto.setId(c.getId());

        if (c.getUtente() != null) {
            dto.setUtente(utenteMapper.mapToDTO(c.getUtente()));
        }

        dto.setAuto1(autoMapper.mapToDTO(c.getAuto1()));
        dto.setAuto2(autoMapper.mapToDTO(c.getAuto2()));

        dto.setPunteggio1(c.getPunteggio1());
        dto.setPunteggio2(c.getPunteggio2());

        if (c.getDataConfronto() != null) {
            dto.setDataConfronto(c.getDataConfronto().toString());
        }

        return dto;
    }
}
package com.progetto.ingsw.backend.mapper;

import com.progetto.ingsw.backend.dto.PreferitoDTO;
import com.progetto.ingsw.backend.model.Preferiti;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PreferitiMapper {

    private final ConfrontoMapper confrontoMapper;
    private final UtenteMapper utenteMapper;

    public PreferitoDTO mapToDTO(Preferiti entity) {
        if (entity == null) return null;

        PreferitoDTO dto = new PreferitoDTO();
        dto.setId(entity.getId());
        dto.setDataAggiunta(entity.getDataAggiunta());
        dto.setUtente(utenteMapper.mapToDTO(entity.getUtente()));
        dto.setConfronto(confrontoMapper.mapToDTO(entity.getConfronto()));
        return dto;
    }
}

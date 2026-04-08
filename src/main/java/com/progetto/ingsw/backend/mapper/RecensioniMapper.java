package com.progetto.ingsw.backend.mapper;

import com.progetto.ingsw.backend.dto.RecensioneDTO;
import com.progetto.ingsw.backend.model.Recensioni;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecensioniMapper {

    private final UtenteMapper utenteMapper;
    private final AutoMapper autoMapper;

    public RecensioneDTO mapToDTO(Recensioni r) {
        if (r == null) return null;

        RecensioneDTO dto = new RecensioneDTO();
        dto.setId(r.getId());
        dto.setVoto(r.getVoto());
        dto.setCommento(r.getCommento());

        if (r.getDataRecensione() != null) {
            dto.setData(r.getDataRecensione().toString());
        }

        if (r.getUtente() != null) {
            dto.setUtente(utenteMapper.mapToDTO(r.getUtente()));
        }

        if (r.getAuto1() != null) {
            dto.setAuto1(autoMapper.mapToDTO(r.getAuto1()));
        }

        if (r.getAuto2() != null) {
            dto.setAuto2(autoMapper.mapToDTO(r.getAuto2()));
        }

        return dto;
    }
}

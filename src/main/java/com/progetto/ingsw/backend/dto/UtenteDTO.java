package com.progetto.ingsw.backend.dto;

import lombok.Data;

@Data
public class UtenteDTO {
    private Integer id;
    private String nome;
    private String cognome;
    private String email;
    private String ruolo;
}

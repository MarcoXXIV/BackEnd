package com.progetto.ingsw.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PreferitoDTO {
    private Integer id;
    private ConfrontoDTO confronto;
    private UtenteDTO utente;
    private LocalDateTime dataAggiunta;
}

package com.progetto.ingsw.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PreferitoDTO {
    private Integer id;           // ID del record "preferiti"
    private Integer confrontoId;  // ID del confronto salvato

    // Dati dell'Auto 1
    private String marcaAuto1;
    private String modelloAuto1;
    private Integer punteggioAuto1;

    // Dati dell'Auto 2
    private String marcaAuto2;
    private String modelloAuto2;
    private Integer punteggioAuto2;

    private LocalDateTime dataAggiunta; // Quando è stato messo nei preferiti
}

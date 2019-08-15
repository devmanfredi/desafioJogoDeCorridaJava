package br.com.desafio;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Piloto {
    private Long id;
    private String nome;
    private LocalDate dataNascimento;
    private LocalDate dataInicioCarreira;
    private BigDecimal dinheiro;
}

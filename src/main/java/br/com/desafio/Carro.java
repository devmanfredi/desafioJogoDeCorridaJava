package br.com.desafio;

import lombok.*;

import java.math.BigDecimal;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Carro {
    private Long id;
    private Long idPiloto;
    private String cor;
    private String marca;
    private Integer Ano;
    private Integer potencia;
    private BigDecimal preco;

    public Optional<Long> getIdPiloto(){
        return Optional.ofNullable(idPiloto);
    }

}

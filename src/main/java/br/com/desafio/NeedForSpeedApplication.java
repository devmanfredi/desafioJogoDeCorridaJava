package br.com.desafio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import br.com.desafio.annotation.Desafio;
import br.com.desafio.app.NeedForSpeedInterface;
import br.com.desafio.exceptions.CarroNaoEncontradoException;
import br.com.desafio.exceptions.IdentificadorUtilizadoException;
import br.com.desafio.exceptions.PilotoNaoEncontradoException;
import br.com.desafio.exceptions.SaldoInsuficienteException;

public class NeedForSpeedApplication implements NeedForSpeedInterface {
    List<Piloto> pilotos = new ArrayList<>();
    List<Carro> carros = new ArrayList<>();
    //List<Marca> marcas = new ArrayList<>();

    @Override
    @Desafio("novoPiloto")
    public void novoPiloto(Long id, String nome, LocalDate dataNascimento, LocalDate dataInicioCarreira, BigDecimal dinheiro) {
        buscarPilotoPorId(id).ifPresent(piloto -> {
            throw new IdentificadorUtilizadoException();
        });
        pilotos.add(Piloto.builder()
                .id(id)
                .nome(nome)
                .dataNascimento(dataNascimento)
                .dataInicioCarreira(dataInicioCarreira)
                .dinheiro(dinheiro)
                .build());
    }

    @Override
    @Desafio("comprarCarro")
    public void comprarCarro(Long id, Long idPiloto, String cor, String marca, Integer ano, Integer potencia, BigDecimal preco) {
        buscarCarroPorId(id).ifPresent(carro -> {
            throw new IdentificadorUtilizadoException();
        });
        buscarPilotoPorId(idPiloto).ifPresent(piloto -> {
            throw new br.com.desafio.exceptions.IdentificadorUtilizadoException();
        });
        Piloto piloto = buscarPilotoPorId(idPiloto).orElseThrow(PilotoNaoEncontradoException::new);
        Carro carro = buscarCarroPorId(id).orElseThrow(CarroNaoEncontradoException::new);
        if (piloto.getDinheiro().compareTo(carro.getPreco()) >= 0) {
            carro.setIdPiloto(piloto.getId());
        } else {
            throw new SaldoInsuficienteException();
        }
    }

    @Override
    @Desafio("venderCarro")
    public void venderCarro(Long idCarro) {
        Carro carro = buscarCarroPorId(idCarro).orElseThrow(CarroNaoEncontradoException::new);
        Piloto piloto = buscarPilotoPorId(carro.getIdPiloto().get()).orElseThrow(PilotoNaoEncontradoException::new);
        piloto.setDinheiro(piloto.getDinheiro().add(carro.getPreco()));
        carro.setIdPiloto(null);
    }

    @Override
    @Desafio("buscarCarroMaisCaro")
    public Long buscarCarroMaisCaro() {
        return carros.stream()
                .sorted(Comparator.comparing(Carro::getId))
                .max(Comparator.comparing(Carro::getPreco))
                .map(Carro::getId)
                .orElseThrow(CarroNaoEncontradoException::new);
    }

    @Override
    @Desafio("buscarCarroMaisPotente")
    public Long buscarCarroMaisPotente() {
        return carros.stream()
                .sorted(Comparator.comparing(Carro::getId))
                .max(Comparator.comparing(Carro::getPotencia))
                .map(Carro::getId)
                .orElseThrow(CarroNaoEncontradoException::new);
    }

    @Override
    @Desafio("buscarCarros")
    public List<Long> buscarCarros(Long idPiloto) {
        buscarPilotoPorId(idPiloto).orElseThrow(PilotoNaoEncontradoException::new);
        return carros.stream()
                .filter(carro -> carro.getIdPiloto().equals(idPiloto))
                .map(Carro::getId).collect(Collectors.toList());
    }

    @Override
    @Desafio("buscarCarrosPorMarca")
    public List<Long> buscarCarrosPorMarca(String marca) {
        buscaCarrosPorMarca(marca).orElseThrow(null);
        return carros.stream()
                .filter(carro -> carro.getMarca().equals(marca))
                .map(Carro::getId)
                .collect(Collectors.toList());
    }

    @Override
    @Desafio("buscarCor")
    public String buscarCor(Long idCarro) {
        buscarCarroPorId(idCarro).orElseThrow(CarroNaoEncontradoException::new);
        return carros.stream()
                .filter(carro -> carro.getId().equals(idCarro))
                .map(Carro::getCor)
                .findFirst().orElseThrow(CarroNaoEncontradoException::new);
    }

    @Override
    @Desafio("buscarMarcas")
    public List<String> buscarMarcas() {
        return carros.stream()
                .map(Carro::getMarca)
                .collect(Collectors.toList());
    }

    @Override
    @Desafio("buscarNomePiloto")
    public String buscarNomePiloto(Long idPiloto) {
        Piloto piloto = buscarPilotoPorId(idPiloto).orElseThrow(PilotoNaoEncontradoException::new);
        return piloto.getNome();
    }

    @Override
    @Desafio("buscarPilotoMaisExperiente")
    public Long buscarPilotoMaisExperiente() {
        return pilotos.stream()
                .sorted(Comparator.comparing(Piloto::getId))
                .max(Comparator.comparing(Piloto::getDataInicioCarreira))
                .map(Piloto::getId)
                .orElseThrow(PilotoNaoEncontradoException::new);
    }

    @Override
    @Desafio("buscarPilotoMenosExperiente")
    public Long buscarPilotoMenosExperiente() {
        return pilotos.stream()
                .sorted(Comparator.comparing(Piloto::getId))
                .min(Comparator.comparing(Piloto::getDataInicioCarreira))
                .map(Piloto::getId)
                .orElseThrow(PilotoNaoEncontradoException::new);
    }

    @Override
    @Desafio("buscarPilotos")
    public List<Long> buscarPilotos() {
        return pilotos.stream()
                .sorted(Comparator.comparing(Piloto::getId))
                .map(Piloto::getId)
                .collect(Collectors.toList());

    }

    @Override
    @Desafio("buscarSaldo")
    public BigDecimal buscarSaldo(Long idPiloto) {
        Piloto piloto = buscarPilotoPorId(idPiloto).orElseThrow(PilotoNaoEncontradoException::new);
        return piloto.getDinheiro();
    }

    @Override
    @Desafio("buscarValorPatrimonio")
    public BigDecimal buscarValorPatrimonio(Long idPiloto) {
        buscarPilotoPorId(idPiloto).orElseThrow(PilotoNaoEncontradoException::new);
        List<BigDecimal> bigDecimalList = carros.stream()
                .filter(carro -> carro.getIdPiloto().equals(idPiloto))
                .map(Carro::getPreco)
                .collect(Collectors.toList());
		return bigDecimalList.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Desafio("trocarCor")
    public void trocarCor(Long idCarro, String cor) {
        throw new UnsupportedOperationException();
    }

    private Optional<Piloto> buscarPilotoPorId(Long id) {
        return pilotos.stream().filter(piloto -> piloto.getId().equals(id)).findFirst();
    }

    private Optional<Carro> buscarCarroPorId(Long id) {
        return carros.stream().filter(carro -> carro.getId().equals(id)).findFirst();
    }

    private Optional<Carro> buscaCarrosPorMarca(String marca) {
        return carros.stream().filter(carro -> carro.getMarca().equals(marca)).findFirst();
    }

}

package service;

import model.Conta;
import model.Transacao;
import model.Pix;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ContaService {

    private final Map<Long, Conta> contas = new HashMap<>();
    private long idCounter = 1;

    public Conta cadastrarConta(Conta conta) {
        if (conta.getNomeTitular() == null || conta.getNomeTitular().isEmpty()) {
            throw new IllegalArgumentException("Nome do titular é obrigatório.");
        }
        if (conta.getNomeTitular() == null || conta.getNomeTitular().isEmpty()) {
            throw new IllegalArgumentException("CPF do titular é obrigatório.");
        }
        if (conta.getDataAbertura().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data de abertura não pode ser no futuro.");
        }
        if (conta.getSaldoInicial() < 0) {
            throw new IllegalArgumentException("Saldo inicial não pode ser negativo.");
        }
        if (!Arrays.asList("corrente", "poupança", "salário").contains(conta.getTipo())) {
            throw new IllegalArgumentException("Tipo de conta inválido.");
        }

        conta.setId(idCounter++);
        contas.put(conta.getId(), conta);
        return conta;
    }

    public List<Conta> listarContas() {
        return new ArrayList<>(contas.values());
    }

    public Conta buscarPorId(Long id) {
        return contas.get(id);
    }

    public List<Conta> buscarPorCpf(String cpf) {
        return contas.values().stream()
                .filter(conta -> conta.getNomeTitular().equals(cpf))
                .collect(Collectors.toList());
    }

    public Conta encerrarConta(Long id) {
        Conta conta = contas.get(id);
        if (conta != null) {
            conta.setAtiva("N");
        }
        return conta;
    }

    public Conta deposito(Transacao transacao) {
        Conta conta = contas.get(transacao.getIdConta());
        if (conta != null && "S".equals(conta.getAtiva())) {
            conta.setSaldoInicial(conta.getSaldoInicial() + transacao.getValor());
            return conta;
        }
        return null;
    }

    public Conta saque(Transacao transacao) {
        Conta conta = contas.get(transacao.getIdConta());
        if (conta != null && "S".equals(conta.getAtiva()) && conta.getSaldoInicial() >= transacao.getValor()) {
            conta.setSaldoInicial(conta.getSaldoInicial() - transacao.getValor());
            return conta;
        }
        return null;
    }

    public Conta pix(Pix pix) {
        Conta origem = contas.get(pix.getIdContaOrigem());
        Conta destino = contas.get(pix.getIdContaDestino());
        if (origem != null && destino != null && "S".equals(origem.getAtiva()) && "S".equals(destino.getAtiva()) && origem.getSaldoInicial() >= pix.getValor()) {
            origem.setSaldoInicial(origem.getSaldoInicial() - pix.getValor());
            destino.setSaldoInicial(destino.getSaldoInicial() + pix.getValor());
            return origem;
        }
        return null;
    }
}

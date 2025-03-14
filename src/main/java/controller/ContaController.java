package controller;

import model.Conta;
import model.Pix;
import model.Transacao;
import service.ContaService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/contas")
public class ContaController {

    private final ContaService contaService;

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @GetMapping("/")
    public String infoProjeto() {
        return "Projeto: Bank - Integrantes: Guilherme Francisco Rm-557648, Anderson Pedro-557002";
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarConta(@RequestBody Conta conta) {
        try {
            Conta novaConta = contaService.cadastrarConta(conta);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaConta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/listar")
    public List<Conta> listarContas() {
        return contaService.listarContas();
    }

    @GetMapping("/buscar/id/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Conta conta = contaService.buscarPorId(id);
        return conta != null ? ResponseEntity.ok(conta) : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conta não encontrada");
    }

    @GetMapping("/buscar/cpf/{cpf}")
    public List<Conta> buscarPorCpf(@PathVariable String cpf) {
        return contaService.buscarPorCpf(cpf);
    }

    @PutMapping("/encerrar/{id}")
    public ResponseEntity<?> encerrarConta(@PathVariable Long id) {
        Conta conta = contaService.encerrarConta(id);
        return conta != null ? ResponseEntity.ok(conta) : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conta não encontrada");
    }

    @PostMapping("/deposito")
    public ResponseEntity<?> deposito(@RequestBody Transacao transacao) {
        Conta conta = contaService.deposito(transacao);
        return conta != null ? ResponseEntity.ok(conta) : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro no depósito");
    }

    @PostMapping("/saque")
    public ResponseEntity<?> saque(@RequestBody Transacao transacao) {
        Conta conta = contaService.saque(transacao);
        return conta != null ? ResponseEntity.ok(conta) : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro no saque");
    }

    @PostMapping("/pix")
    public ResponseEntity<?> pix(@RequestBody Pix pix) {
        Conta conta = contaService.pix(pix);
        return conta != null ? ResponseEntity.ok(conta) : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro na transação Pix");
    }
}

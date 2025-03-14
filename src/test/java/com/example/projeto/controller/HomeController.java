package com.exemplo.projeto.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        String projetoNome = "Projeto Bank";
        String[] integrantes = {"Guilherme Francisco RM_557648", "Anderson Pedro RM_557002"};
               
        String integrantesStr = String.join(", ", integrantes);
        
        return "Projeto: " + projetoNome + " | Integrantes: " + integrantesStr;
    }
}

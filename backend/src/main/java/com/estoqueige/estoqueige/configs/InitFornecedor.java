package com.estoqueige.estoqueige.configs;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.estoqueige.estoqueige.models.Requisitante;
import com.estoqueige.estoqueige.repositories.RequisitanteRepository;

@Configuration
public class InitFornecedor {
    @Bean
    public CommandLineRunner initForne(RequisitanteRepository requisitanteRepository){
        return args -> {
            if(requisitanteRepository.count() == 0){
                Requisitante requisitante = new Requisitante();
                requisitante.setReqNome("FORNECEDOR");
                
                requisitanteRepository.save(requisitante);
                System.out.println("Fornecedor genérico criado.");
            }
        };
    }
}

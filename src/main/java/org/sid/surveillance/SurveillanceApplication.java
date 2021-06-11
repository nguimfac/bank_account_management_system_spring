package org.sid.surveillance;

import org.sid.surveillance.dao.ClientRepository;
import org.sid.surveillance.dao.CompteRepository;
import org.sid.surveillance.dao.OperationRepository;
import org.sid.surveillance.entities.*;
import org.sid.surveillance.metier.IBanqueMetier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Date;

@SpringBootApplication
public class SurveillanceApplication implements CommandLineRunner {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CompteRepository compteRepository;
    @Autowired
    private OperationRepository operationRepository;
    @Autowired
    private IBanqueMetier banqueMetier;

    public static void main(String[] args) {
        SpringApplication.run(SurveillanceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

     Client c1= clientRepository.save(new Client("junior","nguimfack@gmail.com"));
     Client c2= clientRepository.save(new Client("patrick","patrick@gmail.com"));

     Compte cp1= compteRepository.save(new CompteCourant("c1",new Date(),9000,c1,6000));
     Compte cp2= compteRepository.save(new CompteEpargne("c2",new Date(),6000,c2,5.5));

     operationRepository.save(new Versement(new Date(),9000,cp1));
     operationRepository.save(new Versement(new Date(),6000,cp1));
     operationRepository.save(new Versement(new Date(),23000,cp1));
     operationRepository.save(new Retrait(new Date(),6000,cp1));


        operationRepository.save(new Versement(new Date(),11000,cp2));
        operationRepository.save(new Versement(new Date(),78000,cp2));
        operationRepository.save(new Versement(new Date(),68000,cp2));
        operationRepository.save(new Retrait(new Date(),74000,cp2));

        banqueMetier.verser("c1",1111);
    }

}

package org.sid.surveillance.metier;

import org.sid.surveillance.dao.ClientRepository;
import org.sid.surveillance.dao.CompteRepository;
import org.sid.surveillance.dao.OperationRepository;
import org.sid.surveillance.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.websocket.Session;
import java.io.Serializable;
import java.util.Date;
import java.util.Optional;
import java.util.Properties;

@Service
//pour instancier cette class au demarrage
@Transactional
@Component
//pour gerer les transactions
public class BanqueMetierImpl implements IBanqueMetier {
    @Autowired
    private CompteRepository compteRepository;

    @Autowired
    private  ClientRepository clientRepository;

    @Autowired
    private OperationRepository operationRepository;


    @Override
    public Compte consulterCompte(String codeCpte) {
        return compteRepository.findById(codeCpte)
                .orElseThrow(() -> new RuntimeException("Compte Introuvable"));
    }

    @Override
    public void verser(String codeCpte, double montant) {
        Compte cp = consulterCompte(codeCpte);
        Versement v = new Versement(new Date(), montant, cp);
        operationRepository.save(v);

        cp.setSolde(cp.getSolde() + montant);

        compteRepository.save(cp);
    }

    @Override
    public void retirer(String codeCpte, double montant) {
        Compte cp = consulterCompte(codeCpte);
        double facilitesCaisse = 0;
        if (cp instanceof CompteCourant)
            facilitesCaisse = ((CompteCourant) cp).getDecouvert();
        if (cp.getSolde() + facilitesCaisse < montant)
            throw new RuntimeException("Solde Insuffisant");
        Retrait r = new Retrait(new Date(), montant, cp);
        operationRepository.save(r);
        cp.setSolde(cp.getSolde() - montant);
        compteRepository.save(cp);
    }

    @Override
    public void virement(String codeCpte1, String codeCpte2, double montant) {
        if (codeCpte1.equals(codeCpte2)) throw new  RuntimeException("Virement impossible sur le meme compte");
        retirer(codeCpte1, montant);
        verser(codeCpte2, montant);
    }

    @Override
    public Page<Operation> listOperation(String codeCpte, int page, int size) {

        return operationRepository.listOperation(codeCpte, PageRequest.of(page, size));
    }

    @Override
    public void saveClient(Client client) {
        Optional<Client> clientOptional = clientRepository.findByEmail(client.getEmail());
        if(clientOptional.isPresent()) {
            Client cl = clientOptional.get();
            cl.setNom(client.getNom());
            cl.setEmail(client.getEmail());
            clientRepository.save(cl);
        }
        else {
            clientRepository.save(client);
        }
    }

    @Override
    public void makeAccount(Compte compte) {
        compteRepository.save(compte);
    }

    @Override
    public void sendMail(String receiver, String subject, String text) {

    }

}

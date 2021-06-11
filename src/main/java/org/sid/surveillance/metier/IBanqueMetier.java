package org.sid.surveillance.metier;

import org.sid.surveillance.entities.Client;
import org.sid.surveillance.entities.Compte;
import org.sid.surveillance.entities.Operation;
import org.springframework.data.domain.Page;

public interface IBanqueMetier {

    public Compte consulterCompte(String codeCpte);
    public void verser(String codeCpte,double montant);
    public void  retirer(String codeCpte,double montant);
    public void virement(String codeCpte1,String codeCpte2, double montant);
    public Page<Operation> listOperation(String codeCpte,int page ,int size);
    public void saveClient(Client client);
    public void makeAccount(Compte compte);
    public void sendMail(String receiver, String subject, String text);








}

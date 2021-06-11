package org.sid.surveillance.web;

import org.sid.surveillance.dao.ClientRepository;
import org.sid.surveillance.entities.*;
import org.sid.surveillance.metier.IBanqueMetier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;

@Controller
public class BanqueController {

    @Autowired
    private IBanqueMetier banqueMetier;

    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/client")
    public String addClient()
    {
        return "client";
    }

    @RequestMapping("/operations")
    public String index()
    {
        return "comptes.html";
    }

    @RequestMapping("/consulterCompte")
    public String consulter(Model model, String codeCompte,
                            @RequestParam(name = "page",defaultValue = "0")int page,
                            @RequestParam(name = "size",defaultValue = "5") int size )
    {
        model.addAttribute("codeCompte",codeCompte);
        try {
            Compte cp = banqueMetier.consulterCompte(codeCompte);
            Page<Operation> pageOperations = banqueMetier.listOperation(codeCompte,page,size);
            model.addAttribute("list",pageOperations.getContent());
            int[] pages = new  int[pageOperations.getTotalPages()];
            model.addAttribute("pages",pages);
            model.addAttribute("compte",cp);
        }catch (Exception e){
            model.addAttribute("exception",e);
        }
        return "comptes";
    }

    @RequestMapping("/newCompte")
    public String compte()
    {
        return "newCompte";
    }

    @RequestMapping(value = "/saveAccount")
    public String saveAccount(Model model,String email,String nom,String code,String typec)
    {
        if (typec.equals("CE"))
        {
            Client c1= clientRepository.save(new Client(nom,email));
            banqueMetier.makeAccount(new CompteEpargne(code,new Date(),0,c1,5.5));
            model.addAttribute("success1","Compte epargne crée avec success");

        }
        else if (typec.equals("CC"))
        {
            Client c1= clientRepository.save(new Client(nom,email));
            banqueMetier.makeAccount(new CompteCourant(code,new Date(),0,c1,6000));
            model.addAttribute("success2","Compte epargne crée avec success");
        }


        return "client";
    }

    @RequestMapping(value = "/saveOperation",method = RequestMethod.POST)
    public String saveOperation(RedirectAttributes redirectAttributes, Model model, String typeOperation, String codeCompte, double montant, String codeCompte2)
    {
        try {
            if(typeOperation.equals("VERS"))
            {
                banqueMetier.verser(codeCompte,montant);
                redirectAttributes.addFlashAttribute("success","Versement éffectué avec success");
            }

            else if(typeOperation.equals("RET"))
            {
                banqueMetier.retirer(codeCompte,montant);
                redirectAttributes.addFlashAttribute("retrait","Retrait éffectué avec success");

            }
            if(typeOperation.equals("VIR"))
            {
                banqueMetier.virement(codeCompte,codeCompte2,montant);
                redirectAttributes.addFlashAttribute("virement","Virement éffectué avec success");
            }
        }
        catch (Exception e)
        {
            model.addAttribute("error",e);
            return "redirect:/consulterCompte?codeCompte="+codeCompte+"&error="+e.getMessage();
        }
        return "redirect:/consulterCompte?codeCompte="+codeCompte;
    }
}


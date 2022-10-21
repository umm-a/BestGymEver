package inlämningsuppgift2;

import javax.swing.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BestGymEver {
    BestGymEver() {
        boolean test = false;
        boolean visaEjIJOptionPane = true;
        Path filväg = Paths.get("src/inlämningsuppgift2/customers.txt");
        Member member = new Member();
        boolean kör = true;
        while (kör) {
            boolean seStatus = false;
            try {
                String val = JOptionPane.showInputDialog(null, "Vad vill du göra?\n1=Se medlemsstatus\n2=Logga träning\n" +
                        "3=Sök efter något som står i kundens personliga fil\n4=Kolla totala antal träningstillfällen för kund\n5=Avsluta");
                if (val.trim().equals("1")) {
                    seStatus = true;
                    String personnummer = JOptionPane.showInputDialog("Vilken kund? Ange personnummer (10 siffror)").trim();
                    if (member.korrektPersonnummer(personnummer, test) == true) {
                        member.finnsIFil(personnummer, filväg, test, seStatus);
                    }
                } else if (val.trim().equals("2")) {
                    String personnummer = JOptionPane.showInputDialog("Vilken kund? Ange personnummer (10 siffror)").trim();
                    if (member.korrektPersonnummer(personnummer, test) == true && member.finnsIFil(personnummer, filväg, test, seStatus) == 2) {
                        String kundensFilväg = "src/inlämningsuppgift2/" + personnummer + ".txt";
                        member.kundHarTränatTillPTFil(personnummer, filväg, test);
                    } else if (member.korrektPersonnummer(personnummer, visaEjIJOptionPane) == true) {
                        JOptionPane.showMessageDialog(null, "Det går inte att söka för icke-medlem");
                    }
                } else if (val.trim().equals("3")) {//observera att kunden måste ha loggat träning för att det ska finnas en fil att söka i
                    String personnummer = JOptionPane.showInputDialog("Vilken kund vill du söka för? Ange personnummer (10 siffror)").trim();
                    if (member.korrektPersonnummer(personnummer, test) == true && member.finnsIFil(personnummer, filväg, test, seStatus) == 2) {
                        String sökord = JOptionPane.showInputDialog("Ange vad du letar efter i filen\n(t.ex. ett träningstillfälle 2019-07-08)");
                        Path kundensPath = Paths.get((member.returneraPathnamn(personnummer)));
                        member.läsFrånKundensFil(kundensPath, sökord, test);
                    } else if (member.korrektPersonnummer(personnummer, visaEjIJOptionPane) == true) {
                        JOptionPane.showMessageDialog(null, "Det går inte att söka för icke-medlem");
                    }
                } else if (val.trim().equals("4")) {
                    String personnummer = JOptionPane.showInputDialog("Vilken kund vill du söka för? Ange personnummer (10 siffror)").trim();
                    if (member.korrektPersonnummer(personnummer, test) == true && member.finnsIFil(personnummer, filväg, test, seStatus) == 2) {
                        Path kundensPath = Paths.get(member.returneraPathnamn(personnummer));
                        JOptionPane.showMessageDialog(null, "Antal träningstillfällen: " +
                                member.antalTräningstillfällen(kundensPath, test));
                    } else if (member.korrektPersonnummer(personnummer, visaEjIJOptionPane) == true) {
                        JOptionPane.showMessageDialog(null, "Det går inte att söka för icke-medlem");
                    }
                } else if (val.trim().equals("5")) {
                    kör = false;
                } else {
                    JOptionPane.showMessageDialog(null, "Du angav varken 1, 2, 3, 4 eller 5");
                }
            }catch (NullPointerException e){
                kör = false;
            }
        }
    }
    public static void main(String[] args) {
        BestGymEver theBestGymEver = new BestGymEver();
    }
}

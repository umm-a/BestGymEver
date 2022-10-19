package inlämningsuppgift2;

import javax.swing.*;
import java.io.*;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Scanner;

public class Member {
    public boolean korrektPersonnummer(String kund, boolean test){
        boolean okPersonnummer = false;
        int i = kund.length();
        if(test){
            System.out.println(kund + " testas");
        }
        try{
            if(i==10){
                Long.parseLong(kund);
                return okPersonnummer = true;
            }
            if(test){
                System.out.println("\"" + kund + "\" ej 10 siffror");
            }else{
              JOptionPane.showMessageDialog(null, " Ej 10 siffror");
          }
            return okPersonnummer=false;
        } catch (NumberFormatException e){
            if(test){
                System.out.println(kund + " innehåller annat än endast siffror");
            }else{
               JOptionPane.showMessageDialog(null, kund + " är felaktigt personnummer, ange endast siffror i personnummer");
           }
            return okPersonnummer=false;
        }
        catch (Exception e){//det vore märkligt att komma hit!
            if(test){
                System.out.println("\"" + kund + "\" ej giltigt personnummer");
            }else{
              JOptionPane.showMessageDialog(null, kund + " Ej giltigt personnummer");
          }
            e.printStackTrace();
            return okPersonnummer=false;
        }
    }

    public int finnsIFil(String kund, Path path, boolean test, boolean seStatus){
        String rad = "";
        boolean betalat = false;
        try (Scanner scan = new Scanner(path)) {
            while(scan.hasNextLine()){
                rad = scan.nextLine();
                if(rad.contains(kund)){
                    String personnummerOchNamn = rad;
                    String datumet = scan.nextLine();

                    LocalDate idag = LocalDate.now();
                    LocalDate datumFrånFil = LocalDate.parse(datumet.trim());

                    if(harBetalat((datumFrånFil), idag, test)==true){
                        if(test){
                            System.out.println("Har betalat senaste året = MEDLEM\n");
                        }else{
                            if(seStatus){
                                JOptionPane.showMessageDialog(null, personnummerOchNamn + " har betalat senaste året och är medlem.");
                            }
                        }
                        return 2;
                    }else {
                        if(test){
                            System.out.println("Har ej betalat senaste året = FÖREDETTA MEDLEM\n");
                        }else{
                            JOptionPane.showMessageDialog(null, personnummerOchNamn + "... har ej betalat senaste året. Ej medlem.");
                        }
                        return 3;
                    }
                }
            }
            if(test){
                System.out.println("KUND FINNS EJ");
            }else{
                JOptionPane.showMessageDialog(null, "Kund finns ej");
            }
            return 0;
        } catch (NoSuchFileException e) {
            if(test){
                System.out.println(path + "\nFilen finns ej");
            }else{
                JOptionPane.showMessageDialog(null, "Filen finns ej");
            }
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    public boolean harBetalat(LocalDate datumFrånFil, LocalDate valtDatum, boolean test){
        boolean betalat = false;
        if(test) {
            System.out.println("Har betalat senast " + datumFrånFil);
            System.out.println("Dagens datum: " + valtDatum);
        }
        if(datumFrånFil.isAfter(valtDatum.minusYears(1).minusDays(1))){//Vill att ifall datumet är 2022-10-14 så ska även 2021-10-14 gå igenom
            // därav måste ett år och en dag dras av och inte bara ett år
            betalat=true;
        }
        return betalat;
    }

    public String kundHarTränatTillPTFil(String kundPersonnummer, Path frånPath, boolean test){
            String kunduppgifter = "";
            String rad = "";
            LocalDate dagensDatum = LocalDate.now();

            try (Scanner scan = new Scanner(frånPath)) {
                while (scan.hasNextLine()) {
                    rad = scan.nextLine();
                    if (rad.contains(kundPersonnummer)) {
                        kunduppgifter+=rad.trim() + " ";
                    }
                }
            }catch(FileNotFoundException e){
                if(test){
                    System.out.println("Filen finns ej.");
                }else{
                    JOptionPane.showMessageDialog(null, "Filen finns ej");
                }
                e.printStackTrace();
            }catch (Exception e){
                System.out.println("Fel har uppstått.");
                e.printStackTrace();
            }
            String newFileName = "";
            Path p = Paths.get("src/inlämningsuppgift2/customers.txt");
                newFileName = returneraFilnamn(kundPersonnummer);
                skrivTillKundensFil(newFileName, kunduppgifter, dagensDatum, test);
            return newFileName;
    }
    public String skrivTillKundensFil(String newFileName, String kunduppgifter, LocalDate dagensDatum, boolean test) {
        String skrevsTillFil = "";
        try {
            File newFile = new File(newFileName);
            if (!newFile.exists()) {
                newFile.createNewFile();
            }
            try (PrintWriter pwTillFil = new PrintWriter(new FileWriter(newFile, true))) {
                pwTillFil.println(kunduppgifter + "tränade " + dagensDatum);
                skrevsTillFil = kunduppgifter + "tränade " + dagensDatum;
                if(test){
                    System.out.println("\"" + kunduppgifter + "tränade " + dagensDatum + "\"" + " skrevs in i filen");
                } else{
                    JOptionPane.showMessageDialog(null, "\"" + kunduppgifter + "tränade " + dagensDatum + "\"" + " skrevs in i filen");
                }
            } catch (IOException e) {
                System.out.println("Något med filen gick snett...");
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println("Något annat fel? Hur är det möjligt?");
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
            return skrevsTillFil;
        }

        public int antalTräningstillfällen(File fil, boolean test){
        int raderIFil = 0;
            try(Scanner scanRad = new Scanner(fil)){
                while(scanRad.hasNextLine()){
                    if(scanRad.hasNext()){
                        raderIFil++;
                    }
                    scanRad.nextLine();
                }
            }catch (FileNotFoundException e) {
                if(test){
                    System.out.println("Filen hittades ej");
                }else{
                    JOptionPane.showMessageDialog(null, "Filen hittades ej");
                }
                e.printStackTrace();
            } catch (Exception e){
                System.out.println("Scannerproblem");
                e.printStackTrace();
            }
        return raderIFil;
        }
        public boolean läsFrånKundensFil(File kundensFil, String finnsJagIFilen, boolean test){
        boolean finns = false;
        String rad = "";
            try(Scanner scannaInnehåll = new Scanner(kundensFil)){
                while(scannaInnehåll.hasNextLine()) {
                    rad = scannaInnehåll.nextLine();
                    if(rad.contains(finnsJagIFilen)){
                        if(test){
                            System.out.println("\"" + finnsJagIFilen + "\"" + " finns i filen");
                        }else{
                            JOptionPane.showMessageDialog(null, "\"" + finnsJagIFilen + "\"" +" finns i filen " + kundensFil);
                        }
                        return true;
                    }
                }
            }catch (FileNotFoundException e){
                if(test){
                    System.out.println("Fil ej funnen");
                }else{
                    JOptionPane.showMessageDialog(null, "Fil ej funnen");
                }
                e.printStackTrace();
            }catch (Exception e){
                System.out.println("Något gick snett");
                e.printStackTrace();
            }
            if(test){
                System.out.println("\"" + finnsJagIFilen + "\"" + " hittades ej i filen");
            }else{
                JOptionPane.showMessageDialog(null, "\"" + finnsJagIFilen + "\"" + " hittades ej i filen" + kundensFil);
            }
            return finns;
        }
        public String returneraFilnamn(String personnummer){
            String fil = "src/inlämningsuppgift2/" + personnummer + ".txt";
        return fil;
        }

        public boolean raderaFil(File filnamn){
        boolean raderad=false;
            if (filnamn.delete()) {
                System.out.println("Filen raderades: " + filnamn.getName());
                return true;
            } else {
                System.out.println("Filen kunde ej raderas.");
            }
            return raderad;
        }
}



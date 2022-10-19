package inlämningsuppgift2Test;

import inlämningsuppgift2.Member;
import org.junit.jupiter.api.Test;
import inlämningsuppgift2.Member;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*• Läsa personposter från fil.
• todo Ha bra felhantering (relevanta felmeddelanden, exceptionhantering och try-with-resources)
• todo Koden ska vara enkelt läsbar och prydligt skriven.

För att bli Väl Godkänd (VG) måste lösningen uppfylla följande:
• Jobba helt testdrivet när du löser uppgiften (Sigrun kommer såklart inte att kunna se om ni
gör det eller inte, detta är en hederssak)
• Det ska finnas enhetstester som minst täcker följande delar av koden:
o Att inläsning av korrekt data görs, på korrekt sätt
o Att datat kontrolleras och behandlas på rätt sätt
o Att korrekta utskrifter skrivs till fil
*/

public class BestGymEverTests {
    Member member = new Member();
    String kund1 = "9906132324"; //finns ej
    String kund2 = "7608021234"; //finns i fil, betalat
    String kund3 = "7911061234"; //finns i fil, ej betalat
    String kund4 = "123456789"; //finns ej, fel antal siffror i personnummer
    String kund5 = "123456789K"; //finns ej, har korrekt antal tecken men annat än siffror i personnumret

    @Test
    public void seAttFilenCustomersFinnsTest(){
        Path fromFile = Paths.get("src/inlämningsuppgift2/customers.txt");
        try (Scanner scan = new Scanner(fromFile)){
        }catch (FileNotFoundException e){
            System.out.println("File not found");
            e.printStackTrace();
        }catch (Exception e){
            System.out.println("Unexpected error occured");
            e.printStackTrace();
        }
    }

    @Test
    public void finnsKundTest(){
        boolean test = true;
        boolean seStatus = false;
        Path fromFile = Paths.get("src/inlämningsuppgift2/customers.txt");
        Path filSomInteFinns = Paths.get("src/inlämningsuppgift2/finnsEj.txt");

        assert(member.finnsIFil(kund1, fromFile, test, seStatus)==0); //vill se att kunden inte finns i listan
        assert(member.finnsIFil(kund1, filSomInteFinns, test, seStatus)==1);//returnerar 1 om fil ej finns
        assert(member.finnsIFil(kund2, fromFile, test, seStatus)==2); //finns och har betalat
        assert(member.finnsIFil(kund3, fromFile, test, seStatus)==3); //finns och har ej betalat
    }

    @Test
    public void korrektPersonnummerTest(){
        boolean test = true;
        assertFalse(member.korrektPersonnummer(kund4, test)); //fel personnummer ty det saknar en siffra
        assertFalse(member.korrektPersonnummer(kund5, test)); //fel personnummer ty det innehåller en bokstav, dock korrekt längd (10)
        assertTrue(member.korrektPersonnummer(kund2, test));
        assertTrue(member.korrektPersonnummer(kund3, test));//^kund2 samt kund3 ska ha passerat korrektPersonnummer
    }

    @Test
    public void betalningTest(){
        boolean test = true;
        LocalDate date = LocalDate.of(2022, 10, 14);
        assertTrue(member.harBetalat(LocalDate.parse("2022-01-30"), date, test));//denna kund har betalat årsavgiften, ska bli sant
        System.out.println("\n");
        assertFalse(member.harBetalat(LocalDate.parse("2019-01-09"), date, test));//denna kund har inte betalat senaste året, ska bli falskt
        System.out.println("\n");
        assertTrue(member.harBetalat(LocalDate.parse("2021-10-14"), date, test)); //ett år sedan på dagen ska ge true som utslag
    }

    @Test
    public void kopieraTillPTFilTest(){
        boolean test = true;
        Path p = Paths.get("src/inlämningsuppgift2/customers.txt");
        assert(member.kundHarTränatTillPTFil(kund2, p, test).equals("src/inlämningsuppgift2/7608021234.txt")); //7608021234, Diamanda Djedi 2022-10-15
        //vi kollar av ifall det filnamn vi tänkt ge "src/sprint2/inlämningsuppgift2/7608021234.txt" returneras, för då får vi en korrekt skapad fil från metoden (med korrekt personnummer)
    }

    @Test
    public void skrivTillKundensFilTest(){
        //vi vill i metoden skapa upp en ny fil med kundens personnr som namn för filen. I filen skriver vi in namn, personnummer och datum då personen tränat på en rad
        boolean test = true;
        LocalDate testDatum = LocalDate.of(2022, 10, 14);
        assert(member.skrivTillKundensFil("src/inlämningsuppgift2/9906132222.txt", "9906132222, Maria Hagelin ", testDatum, test).equals("9906132222, Maria Hagelin tränade 2022-10-14"));
        assertFalse(member.skrivTillKundensFil("src/inlämningsuppgift2/9906132222.txt", "9906132222, Maria Hagelin ", testDatum, test).equals("Maria Hagelin är kort"));

        File minFil = new File("src/inlämningsuppgift2/9906132222.txt");
        assert(member.raderaFil(minFil)==true); //filen raderas för att jag vill få en mer "clean virtuell arbetsyta"(inte fullklottrade filer...)
    }

    @Test
    public void antalTräningstillfällen(){
        boolean test = true;
        LocalDate testDatum = LocalDate.of(2022, 10, 14);

        member.skrivTillKundensFil("src/inlämningsuppgift2/1234567890.txt", "Ajlajk Chocolaat", testDatum, test);
        member.skrivTillKundensFil("src/inlämningsuppgift2/1234567890.txt", "Ajlajk Chocolaat", testDatum, test);

        File minFil = new File("src/inlämningsuppgift2/1234567890.txt");
        assert(member.antalTräningstillfällen(minFil, test)==2);
        assert(member.raderaFil(minFil)==true);//filen måste raderas för att testerna ska gå gröna
        //Har däremot lagt till utskrifter från metoden som visar på att filen funnits under testets gång!
    }

    @Test
    public void läsFrånKundensFilTest(){
        boolean test = true;
        File minFil = new File("src/inlämningsuppgift2/testfil.txt");

        assertFalse(member.läsFrånKundensFil(minFil, "Björnen sover", test));//skickar in vad vi vill leta efter i filen, returnerar false då "Björnen sover" ej finns i filen
        assertTrue(member.läsFrånKundensFil(minFil, "Pirajor är inte ett dugg", test)); //finns i filen testfil.txt
        //vi kan också använda denna metod för att se om vi fått en fullständig korrekt utskrift sedan skrivTillFilTest(), här kollar vi istället i min testfil.txt

        LocalDate testDatum = LocalDate.of(2022, 10, 14);
        member.skrivTillKundensFil("src/inlämningsuppgift2/9906132222.txt", "9906132222, Maria Hagelin ", testDatum, test);
        File MariasFil = new File("src/inlämningsuppgift2/9906132222.txt");
        assertTrue(member.läsFrånKundensFil(MariasFil, "9906132222, Maria Hagelin tränade 2022-10-14", test));//kollar efter korrekt utskrift till fil från läsKundensFil
        assert(member.raderaFil(MariasFil)==true);
    }

    @Test
    public void returneraFilnamnTest(){
        String ettPersonnummer = "9507143421";
        assert(member.returneraFilnamn(ettPersonnummer).equals("src/inlämningsuppgift2/9507143421.txt"));
    }
}

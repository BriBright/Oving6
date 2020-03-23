import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class Konto_klient {
    public static void main(String args[]) throws NullPointerException {
        EntityManagerFactory emf = null;
        KontoDAO fasade = null;
        System.out.println("Starts...");
        try {
            emf = Persistence.createEntityManagerFactory("pu");

            System.out.println("konstruktør ferdig " + emf);
            fasade = new KontoDAO((emf));
            System.out.println("konstruktor ferdig");

            //Oppretter konto med setMetodene
            Konto konto = new Konto();
            konto.setKontonr("32010004444");
            konto.setSaldo(25000.9);
            konto.setEier("Navn Navnesen");
            fasade.opprettNyKonto(konto);

            //oppretterny konto med konstruktor i stedet for setMetodene
            konto = new Konto("2400666772", 300.0, "Ola Normann");
            fasade.opprettNyKonto(konto);

            konto = new Konto("2400666662", 650, "Kari Normann");
            fasade.opprettNyKonto(konto);

            konto = new Konto("0000000000", 10000, "Even Evensen");
            fasade.opprettNyKonto(konto);


            //Skriv ut alle kontoene som er lagret
            System.out.println("Følgende kontoer er lagret i DB:");
            List<Konto> liste = fasade.getAlleKontoer();
            for (Konto k : liste) {
                System.out.println("---" + k);
            }

            //lister ut alle kontoer med saldo større enn gitt beløp
            liste = fasade.getKontoMedSaldo(1000);
            System.out.println("Følgenede har mer enn 1000kr i saldo: " + liste.size());
            for (Konto k : liste) {
                System.out.println("\t" + k.getEier() + ", saldo: " + k.getSaldo());
            }

            //Endre på eier
            konto = (Konto) liste.get(0);
            konto.setEier("Endret eier");
            fasade.endreKonti(konto);

            konto = fasade.finnKonto(konto.getKontonr());
            System.out.println("Eier er nå endret til " + konto);

            /*
            //Trekk beløp
            konto = (Konto)liste.get(0);
            konto.trekk(2000);
            fasade.endreKonti(konto);

            konto = fasade.finnKonto(konto.getKontonr());
            System.out.println("Nåværende saldo:" + konto.getSaldo());
            */

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            emf.close();
        }
    }
}

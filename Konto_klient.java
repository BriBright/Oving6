import org.eclipse.persistence.internal.oxm.schema.model.All;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;

public class Konto_klient {
    public static void main(String args[]) throws NullPointerException {
        EntityManagerFactory emf = null;
        KontoDAO kontoDAO = null;
        System.out.println("Starter...");
        try {
            emf = Persistence.createEntityManagerFactory("pu");
            kontoDAO = new KontoDAO((emf));

            Konto k1 = new Konto("333444555",5000,"Brigitt");
            Konto k2 = new Konto("222333444", 300, "Helle");
            Konto k3 = new Konto("1114433", 2500, "Inga");
            kontoDAO.opprettNyKonto(k1);
            kontoDAO.opprettNyKonto(k2);
            kontoDAO.opprettNyKonto(k3);


            //Skriv ut alle kontoene som er lagret
            System.out.println("Følgende kontoer er lagret i DB:");
            List<Konto> liste = kontoDAO.getAlleKontoer();
            for (Konto k : liste) {
                System.out.println("---" + k);
            }
            
            System.out.println("\n");

            //lister ut alle kontoer med saldo større enn gitt beløp
            liste = kontoDAO.getKontoMedSaldo(1000);
            System.out.println("Følgenede har mer enn 1000kr i saldo: " + liste.size() + "stk:");
            for (Konto k : liste) {
                System.out.println("\t" + k.toString());
            }

            System.out.println("\n");

            //Endre på eier
            Konto konto = (Konto) liste.get(0);
            konto.setEier("Per");
            kontoDAO.endreKonti(konto);

            konto = kontoDAO.finnKonto(konto.getKontonr());
            System.out.println("Eier er nå endret til " + konto.getEier() + " med kontonr: "+ konto.getKontonr() + "\n");

            List<Konto> liste2 = kontoDAO.getAlleKontoer();
            for (Konto k : liste2) {
                System.out.println("---" + k);
            }

            double belop = 500;
            kontoDAO.overforing(belop,"333444555","222333444");

            k1 = kontoDAO.finnKonto("333444555");
            k2 = kontoDAO.finnKonto("222333444");
            System.out.println("Overfører " + belop + " fra " + k1.getEier()+ " til " +
                        k2.getEier()+ "\nNåværende saldo: " + "\n" + k1.toString() + "\n" + k2.toString());

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            emf.close();
        }
    }
}

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
            konto.setEier("Hei hei Halloooåå");
            kontoDAO.endreKonti(konto);

            konto = kontoDAO.finnKonto(konto.getKontonr());
            System.out.println("Eier er nå endret til " + konto.getEier() + " med kontonr: "+ konto.getKontonr() + "\n");

            //Overføring
            Konto overførFra = (Konto)liste.get(1);
            Konto overførTil = (Konto)liste.get(0);

            double belop = 1500;
            kontoDAO.overforing(belop,overførFra.getKontonr(),overførTil.getKontonr());

            overførFra = kontoDAO.finnKonto(overførFra.getKontonr());
            overførTil = kontoDAO.finnKonto(overførTil.getKontonr());
            System.out.println("Overfører " + belop + " fra " + overførFra.getEier()+ " til " +
                        overførTil.getEier()+ "\nNåværende saldo: " + "\n" + overførFra.toString() + "\n" + overførTil.toString());

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            emf.close();
        }
    }
}

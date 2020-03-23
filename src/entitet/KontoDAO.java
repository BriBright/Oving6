package entitet;

import javax.persistence.*;
import java.util.*;

public class KontoDAO {
    private EntityManagerFactory emf;

    private EntityManager getEM() { return emf.createEntityManager(); }
    private void lukkEM(EntityManager em) { if (em != null && em.isOpen()) em.close(); }
    public KontoDAO(EntityManagerFactory emf) { this.emf = emf; }


    public void opprettNyKonto(Konto konto) {
        EntityManager em = getEM();
        try {
            em.getTransaction().begin();
            em.persist(konto);
            em.getTransaction().commit();
        } finally {
            lukkEM(em);
        }
    }

    //finn bok basert på kontonr
    public Konto finnKonto(String kontonr) {
        EntityManager em = getEM();
        try {
            return em.find(Konto.class, kontonr);
        } finally {
            lukkEM(em);
        }
    }

    public void endreKonti(Konto konto) {
        EntityManager em = getEM();
        try {
            em.getTransaction().begin();
            Konto k = em.merge(konto);
            em.getTransaction().commit();
        } finally {
            lukkEM(em);
        }
    }

    public void slettKonto(String kontonr) {
        EntityManager em = getEM();
        try {
            Konto k = finnKonto(kontonr);
            em.getTransaction().begin();
            em.remove(k);
            em.getTransaction().commit();
        } finally {
            lukkEM(em);
        }
    }

    public List<Konto> getAlleKontoer() {
        EntityManager em = getEM();
        try {
            Query q = em.createQuery("SELECT OBJECT(k) FROM Konto k");
            return q.getResultList();
        } finally {
            lukkEM(em);
        }
    }

    public List<Konto> getKontoMedSaldo(double belop) {
        EntityManager em = getEM();
        try {
            Query q = em.createQuery("SELECT OBJECT (a) FROM Konto a WHERE a.saldo > :belop");
            q.setParameter("belop", belop);
            return q.getResultList();
        } finally {
            lukkEM(em);

        }
    }


    //liten testklient
    public static void main(String args[]) throws NullPointerException{
        EntityManagerFactory emf = null;
        KontoDAO fasade = null;
        System.out.println("starter2...");
        try{
            emf = Persistence.createEntityManagerFactory("LeksjonStandaloneEntitetPU");
            //LeksjonStandaloneEntitetPU=Persistence Unit Name, se persistence.xml
            System.out.println("konstruktor ferdig " + emf);
            fasade = new KontoDAO((emf));
            System.out.println("konstruktor ferdig");

            //Oppretter konto med setMetodene
            Konto konto = new Konto();
            konto.setKontonr("32010004444");
            konto.setSaldo(25000.9);
            konto.setEier("Navn Navnesen");
            fasade.opprettNyKonto(konto);

            //oppretterny konto med konstruktor i stedet for setMetodene
            konto = new Konto("2400666772",300.0,"Ola Normann");//tar alle parametre Id som lages automatisk
            fasade.opprettNyKonto(konto);

            konto = new Konto("2400666662",650,"Kari Normann");
            fasade.opprettNyKonto(konto);

            konto = new Konto("0000000000",10000,"Even Evensen");
            fasade.opprettNyKonto(konto);


            //Skriv ut alle kontoene som er lagret
            System.out.println("Følgende kontoer er lagret i DB:");
            List<Konto> liste = fasade.getAlleKontoer();
            for (Konto k : liste){
                System.out.println("---" + k);
            }

            //lister ut alle kontoer med saldo større enn gitt beløp
            liste = fasade.getKontoMedSaldo(1000);
            System.out.println("Følgenede har mer enn 1000kr i saldo: " + liste.size());
            for (Konto k : liste){
                System.out.println("\t" + k.getEier() + ", saldo: " + k.getSaldo());
            }

            //Endre på eier
            konto = (Konto) liste.get(2);
            konto.setEier("Endret eier");
            fasade.endreKonti(konto);

            konto = fasade.finnKonto(konto.getKontonr());
            System.out.println("Eier er nå endret til " + konto);


            //Trekk beløp
            konto = (Konto)liste.get(0);
            konto.trekk(2000);
            fasade.endreKonti(konto);

            konto = fasade.finnKonto(konto.getKontonr());
            System.out.println("Nåværende saldo:" + konto.getSaldo());


        }catch (Exception e){
            e.printStackTrace();
        }finally{
            emf.close();
        }
    }
}


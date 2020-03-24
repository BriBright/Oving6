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

    //finn konto basert på kontonr
    public Konto finnKonto(String kontonr) {
        EntityManager em = getEM();
        try {
            return em.find(Konto.class, kontonr);
        } finally {
            lukkEM(em);
        }
    }

    //Gjør endringer på konto
    public void endreKonti(Konto konto) {
        EntityManager em = getEM();
        try {
            em.getTransaction().begin();
            Konto k = konto;
            em.merge(k);
            em.getTransaction().commit();
        } finally {
            lukkEM(em);
        }
    }

    //Hent ut alle kontoene
    public List<Konto> getAlleKontoer() {
        EntityManager em = getEM();
        try {
            Query q = em.createQuery("SELECT OBJECT(k) FROM Konto k");
            return q.getResultList();
        } finally {
            lukkEM(em);
        }
    }

    // Finn konto med saldo over et gitt beløp
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

    private void trekkPenger(double belop, Konto k){
        EntityManager em = getEM();
        try {
            em.getTransaction().begin();
            Konto midl = k;
            midl.trekk(belop);
            em.merge(midl);
            em.getTransaction().commit();
        }finally {
            lukkEM(em);
        }
    }

    public void settInnPenger(double belop, Konto k){
        EntityManager em = getEM();
        try {
            em.getTransaction().begin();
            Konto midl = k;
            midl.settInnPeng(belop);
            em.merge(midl);
            em.getTransaction().commit();
        }finally {
            lukkEM(em);
        }
    }

    public void overforing(double belop, String fra, String til){
        EntityManager em = getEM();
        boolean ok = false;

        while (!ok)
            try {
                Konto fraKonto = em.find(Konto.class, fra);
                Konto tilKonto = em.find(Konto.class, til);

                em.getTransaction().begin();
                trekkPenger(belop, fraKonto);
                settInnPenger(belop, tilKonto);
                ok = true;
                em.merge(fraKonto);
                em.merge(tilKonto);

                em.getTransaction().commit();
            }catch (OptimisticLockException ole){
                ole.printStackTrace();
            }finally {
                lukkEM(em);
            }
    }
}


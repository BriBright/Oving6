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
}


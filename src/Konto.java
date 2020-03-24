import javax.persistence.*;
import java.io.*;
import java.util.*;

@Entity

public class Konto implements Serializable {
    @Id
    @Column(name = "KONTONR")
    private String kontonr;

    @Version
    @Column(name = "optima_lock")
    private int lock;

    private double saldo;
    private String eier;

    public Konto() {
    }

    public Konto(String kontonr, double saldo, String eier) {
        this.kontonr = kontonr;
        this.saldo = saldo;
        this.eier = eier;
        this.lock = 0;
    }

    public String getKontonr() { return kontonr; }

    public void setKontonr(String kontonr) { this.kontonr = kontonr; }

    public double getSaldo(){ return saldo; }

    public void setSaldo(double saldo){ this.saldo = saldo; }

    public int getLock(){ return lock; }

    public void setLock(int lock){
        this.lock = lock;
    }

    public String getEier() { return eier; }

    public void setEier(String eier) { this.eier = eier; }

    public void trekk(double beløp){
        double res = getSaldo() - beløp;
        setSaldo(res);
    }
    public void settInnPeng(double beløp){
        double res = getSaldo() + beløp;
        setSaldo(res);
    }

    public String toString() {
        return "Konto eier: " + eier + ", kontonr:" + kontonr + ". Har en saldo på: " + saldo;
    }
}

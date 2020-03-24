DROP TABLE IF EXISTS KONTO;

CREATE TABLE KONTO(
  kontonr varchar(50) NOT NULL,
  saldo double,
  eier varchar(50) NOT NULL,
  optima_lock integer NOT NULL,
  PRIMARY KEY (kontonr)
);
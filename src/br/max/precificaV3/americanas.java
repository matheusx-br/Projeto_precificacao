package br.max.precificaV3;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class americanas {

    public void Execamericanas() {

        try {

                Statement consulta = new ConectaDB().jdbc().createStatement();
                String sql = "SELECT * FROM TGFPRO INNER JOIN TGFEXC ON TGFPRO.CODPROD = TGFEXC.CODPROD WHERE NOT AD_SKUB2W IS NULL AND NUTAB = (select max(NUTAB) from tgftab where CODTAB=1) AND AD_PROCESSADO is null and ROWNUM < = 60 ORDER BY AD_PRECO_BUYBOX DESC";
                ResultSet sqlite = null;
                sqlite = consulta.executeQuery(sql);
                while (sqlite.next()) {
                    String loja = null;
                    String preco = null;
                    String AD_SKU = sqlite.getString("AD_SKUB2W");
                    BigDecimal CODPRO = sqlite.getBigDecimal("CODPROD");
                    String CODPROD = CODPRO.toString();
                    Statement processado = new ConectaDB().jdbc().createStatement();
                    processado.executeUpdate("UPDATE TGFEXC SET AD_PROCESSADO = 'S' WHERE NUTAB= (select max(NUTAB) from tgftab where CODTAB=1) AND CODPROD ="+CODPROD);
                    String url = "http://www.americanas.com.br/produto/" + AD_SKU;
                    String responseD = new ObtemIP().main(url);
                    int ic = responseD.indexOf("src__BestPrice-sc-1jvw02c-5 cBWOIB priceSales") + 57;
                    int f = ic + 7;
                    int in = responseD.indexOf("sold-and-delivered__Link-sc-1hkd1iz-0 kMfUlh") + 46;
                    int fi = in + 7;

                    preco = responseD.substring(ic,f);
                    loja = responseD.substring(in, fi);

                    if (!"E-conic".equals(loja)) {
                        loja = "Concorrente";
                    }
                    if(loja != null) {

                        Statement tgfexc = new ConectaDB().jdbc().createStatement();
                        tgfexc.executeUpdate("UPDATE TGFEXC SET AD_PRECO_BUYBOX = '"+preco+"' WHERE NUTAB= (select max(NUTAB) from tgftab where CODTAB=1) AND CODPROD ="+CODPROD);
                        tgfexc.executeUpdate("UPDATE TGFEXC SET AD_LOJA = '"+loja+"', AD_DATA_BUYBOX = SYSDATE WHERE NUTAB= (select max(NUTAB) from tgftab where CODTAB=1) AND CODPROD ="+CODPROD);
                        Statement log = new ConectaDB().jdbc().createStatement();
                        log.executeUpdate("INSERT INTO LOG_PRECO (DTINSERIDO,CODPROD, SITE, LOJA, PRECO) VALUES (SYSDATE,"+CODPROD+",'"+url+"','"+loja+"',replace(replace('"+preco+"','<',''),'>','$'))");

                        System.out.println("comeca aqui -->"+url+"<-- termina aqui");
                        System.out.println("comeca aqui -->"+preco+"<-- termina aqui");
                        System.out.println("comeca aqui -->"+loja+"<-- termina aqui");
                        Thread.sleep(10000);
                    }
                }
                Statement fim = new ConectaDB().jdbc().createStatement();

                fim.executeUpdate("UPDATE TGFEXC SET AD_PROCESSADO = NULL WHERE NOT AD_PROCESSADO IS NULL AND NUTAB = (select max(NUTAB) from tgftab where CODTAB=1)");
                new submarino().Execsubmarino();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                Statement reset = new ConectaDB().jdbc().createStatement();
                reset.executeUpdate("UPDATE TGFEXC SET AD_PROCESSADO = NULL WHERE NOT AD_PROCESSADO IS NULL AND NUTAB = (select max(NUTAB) from tgftab where CODTAB=1)");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
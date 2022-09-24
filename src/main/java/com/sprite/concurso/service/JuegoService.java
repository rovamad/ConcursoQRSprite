package com.sprite.concurso.service;

import com.sprite.concurso.pojo.AbstractMethod;
import com.sprite.concurso.pojo.QR;
import com.sprite.concurso.pojo.ReglaDeJuego;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Slf4j
@Service
public class JuegoService {
    ReglaDeJuego reglaDeJuego = new ReglaDeJuego();

    public ResponseEntity<String> setReglas(ReglaDeJuego payload, Integer tier1, Integer tier2) {
        tier1 = (tier1 == null) ? 0 : tier1;
        tier2 = (tier2 == null) ? 0 : tier2;

        this.reglaDeJuego=payload;
        int cantQRs = reglaDeJuego.getQrConcursantes().size();

        if (cantQRs == 0) {
            return new ResponseEntity<>("Falta configurar QRs Concursantes.", HttpStatus.CREATED);
        } else if (tier1 + tier2 > cantQRs) {
            return new ResponseEntity<>("Hay mas Premios a repartir que Codigos concursando.", HttpStatus.CREATED);
        } else {
            for (int i = 0; i < cantQRs; i++) {
                reglaDeJuego.getQrConcursantes().get(i).setQrLeido(false);
            }

            ramdomizeTiers(tier1, tier2);
        }

        return new ResponseEntity<>("Ok", HttpStatus.CREATED);
    }

    private void ramdomizeTiers(int premiosTier1, int premiosTier2) {
        List<Integer> posicionQRGanadorTier1 = new ArrayList<>();
        List<Integer> posicionQRGanadorTier2 = new ArrayList<>();

        if (premiosTier1 > 0) {
            posicionQRGanadorTier1 = ramdomizeLogica(premiosTier1);
        }

        if (premiosTier2 > 0) {
            posicionQRGanadorTier2 = ramdomizeLogica(premiosTier2);

            for (int i = 0; i< posicionQRGanadorTier2.size() && premiosTier1 > 0; i++){
                if (posicionQRGanadorTier1.contains(posicionQRGanadorTier2.get(i))) {
                    posicionQRGanadorTier2.set(i, AbstractMethod.generaPosicionAleatoria(0, reglaDeJuego.getQrConcursantes().size() - 1));
                    i--;
                }
            }
        }

        configurandoQRsganadores(posicionQRGanadorTier1, 1);
        configurandoQRsganadores(posicionQRGanadorTier2, 2);
    }

    private void configurandoQRsganadores(List<Integer> posicionQRGanador, int tier) {
        for (int i = 0; i< posicionQRGanador.size(); i++){
            reglaDeJuego.getQrConcursantes().get(posicionQRGanador.get(i)).setTier(tier);
        }
    }

    private List<Integer> ramdomizeLogica(int premiosTier) {
        List<Integer> result = new ArrayList<>();
        
        for (int i = 0; i < premiosTier; i++) {
            int valorAGuardar = AbstractMethod.generaPosicionAleatoria(0, reglaDeJuego.getQrConcursantes().size() - 1);
            if (!result.contains(valorAGuardar)) {
                result.add(valorAGuardar);
            } else {
                i--;
            }
        }
        
        return result;
    }

    public String resultado(String code) {
        String html = "https://sprite.webmark.cl/tier00.html";
        //String html = "Tier0.html";

        if (reglaDeJuego == null || reglaDeJuego.getQrConcursantes().size() == 0) {
            return "Error.html";
        } else {
            for (int i = 0; i< reglaDeJuego.getQrConcursantes().size(); i++) {
                if (reglaDeJuego.getQrConcursantes().get(i).getCodigo().equals(code) &&
                        reglaDeJuego.getQrConcursantes().get(i).getTier() == 1 &&
                        reglaDeJuego.getQrConcursantes().get(i).getQrLeido() == false &&
                        reglaDeJuego.getQrConcursantes().get(i).getFechaActual() == null) {

                    String dateString = crearFechaActual();

                    reglaDeJuego.getQrConcursantes().get(i).setQrLeido(true);
                    reglaDeJuego.getQrConcursantes().get(i).setFechaActual(dateString);

                    html = "https://sprite.webmark.cl/tier01.html?c="+code+dateString;
                    //html = "Tier1.html?c="+code+dateString;
                } else if (reglaDeJuego.getQrConcursantes().get(i).getCodigo().equals(code) &&
                        reglaDeJuego.getQrConcursantes().get(i).getTier() == 2 &&
                        reglaDeJuego.getQrConcursantes().get(i).getQrLeido() == false &&
                        reglaDeJuego.getQrConcursantes().get(i).getFechaActual() == null) {

                    String dateString = crearFechaActual();

                    reglaDeJuego.getQrConcursantes().get(i).setQrLeido(true);
                    reglaDeJuego.getQrConcursantes().get(i).setFechaActual(dateString);

                    html = "https://sprite.webmark.cl/tier02.html?c="+code+dateString;
                    //html = "Tier2.html?c="+code+dateString;
                }
            }
        }

        return html;
    }

    private String crearFechaActual() {
        Timestamp timestamp = null;
        timestamp = new Timestamp(System.currentTimeMillis());
        Date date=timestamp;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-3"));
        String dateString = sdf.format(date);

        return dateString;
    }

    public List<QR> consultarGanadoresActuales() {
        List<QR> qrsLeidos = new ArrayList<>();

        for (int i = 0; i< reglaDeJuego.getQrConcursantes().size(); i++) {
            if (reglaDeJuego.getQrConcursantes().get(i).getQrLeido() == true) {
                qrsLeidos.add(reglaDeJuego.getQrConcursantes().get(i));
            }
        }
        return qrsLeidos;
    }
}

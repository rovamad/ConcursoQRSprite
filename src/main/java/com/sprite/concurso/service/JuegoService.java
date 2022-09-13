package com.sprite.concurso.service;

import com.sprite.concurso.pojo.AbstractMethod;
import com.sprite.concurso.pojo.QR;
import com.sprite.concurso.pojo.ReglaDeJuego;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class JuegoService {

    /*@Autowired*/
    ReglaDeJuego reglaDeJuego = new ReglaDeJuego();

    public ResponseEntity<String> setReglas(ReglaDeJuego payload) {
        this.reglaDeJuego=payload;
        int cantQRs = reglaDeJuego.getQrConcursantes().size();

        if (cantQRs == 0) {
            return new ResponseEntity<>("Falta configurar QRs Concursantes.", HttpStatus.CREATED);
        } else if (payload.getPremiosTier1() + payload.getPremiosTier2() > cantQRs) {
            return new ResponseEntity<>("Hay mas Premios a repartir que Codigos concursando.", HttpStatus.CREATED);
        } else {
            for (int i = 0; i < cantQRs; i++) {
                reglaDeJuego.getQrConcursantes().get(i).setQrLeido(false);
            }

            ramdomizeTiers(payload.getPremiosTier1(), payload.getPremiosTier2());
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
        String hmtl = "Tier0.html";

        if (reglaDeJuego == null || reglaDeJuego.getQrConcursantes().size() == 0) {
            return "Error.html";
        } else {
            for (int i = 0; i< reglaDeJuego.getQrConcursantes().size(); i++) {
                if (reglaDeJuego.getQrConcursantes().get(i).getCodigo().equals(code) &&
                        reglaDeJuego.getQrConcursantes().get(i).getTier() == 1 &&
                        reglaDeJuego.getQrConcursantes().get(i).getQrLeido() == false) {
                    reglaDeJuego.getQrConcursantes().get(i).setQrLeido(true);

                    hmtl = "Tier1.html";
                } else if (reglaDeJuego.getQrConcursantes().get(i).getCodigo().equals(code) &&
                        reglaDeJuego.getQrConcursantes().get(i).getTier() == 2 &&
                        reglaDeJuego.getQrConcursantes().get(i).getQrLeido() == false) {

                    reglaDeJuego.getQrConcursantes().get(i).setQrLeido(true);

                    hmtl = "Tier2.html";
                }
            }
        }

        return hmtl;
    }

    public List<QR> consultarEstadoActual() {
        return reglaDeJuego.getQrConcursantes();
    }
}

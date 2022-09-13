package com.sprite.concurso.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ReglaDeJuego {

    private int premiosTier1;
    private int premiosTier2;
    private List<QR> qrConcursantes;
}

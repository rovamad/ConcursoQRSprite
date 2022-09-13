package com.sprite.concurso.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QR {

    private String codigo;
    private boolean qrLeido;
    private int tier;

    public boolean getQrLeido() {
        return qrLeido;
    }
}

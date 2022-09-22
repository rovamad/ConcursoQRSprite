package com.sprite.concurso.pojo;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class QR {

    private String codigo;
    private boolean qrLeido;
    private int tier;
    private Timestamp timestamp;

    public boolean getQrLeido() {
        return qrLeido;
    }
}

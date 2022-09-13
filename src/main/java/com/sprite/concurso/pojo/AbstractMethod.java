package com.sprite.concurso.pojo;

/**
 * @author Discoduroderoer
 */
public class AbstractMethod {

    /**
     * Genera un numero aleatorio entre dos numeros
     * @param minimo
     * @param maximo
     * @return 
     */
    public static int generaPosicionAleatoria(int minimo, int maximo) {
        int num = (int) Math.floor(Math.random() * (maximo - minimo + 1) + (minimo));
        return num;
    }

}

package com.sprite.concurso.controller;

import com.sprite.concurso.pojo.QR;
import com.sprite.concurso.pojo.ReglaDeJuego;
import com.sprite.concurso.service.JuegoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Slf4j
@RestController
public class ConcursoController {

    @Autowired
    JuegoService juegoService;

    @PostMapping(value = "/reglas", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<String> setReglas(@RequestBody ReglaDeJuego payload, @RequestParam Integer tier1, @RequestParam Integer tier2) throws IOException {
        return juegoService.setReglas(payload, tier1, tier2);
    }

    @GetMapping(value = "/jugar", produces = {MediaType.TEXT_HTML_VALUE})
    public void jugar(HttpServletResponse response, @RequestParam String c) throws IOException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Date date=timestamp;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy-hh:mm:ss a");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-3"));
        String dateString = sdf.format(date);

        response.sendRedirect(juegoService.resultado(c, dateString)+"?c="+c+dateString);

    }

    @GetMapping(value = "/revisar")
    public List<QR> consultarEstadoActual() throws IOException {

        return juegoService.consultarEstadoActual();
    }

    @GetMapping(value = "ping", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String ping() {
        return "OK";
    }
}

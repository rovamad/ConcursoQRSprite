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
import java.util.List;

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
    public void jugar(HttpServletResponse response, @RequestParam String c) throws IOException {response.sendRedirect(juegoService.resultado(c));
    }

    @GetMapping(value = "/revisar")
    public List<QR> consultarEstadoActual() throws IOException {

        return juegoService.consultarGanadoresActuales();
    }

    @GetMapping(value = "/logs")
    public StringBuilder consultarLogsdeGanadores() throws IOException {

        return juegoService.consultarLogsdeGanadores();
    }

    @GetMapping(value = "ping", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String ping() {
        return "OK";
    }
}

package com.sprite.concurso.controller;

import com.sprite.concurso.helper.JSONHelper;
import com.sprite.concurso.pojo.QR;
import com.sprite.concurso.pojo.ReglaDeJuego;
import com.sprite.concurso.service.JuegoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
public class ConcursoController {

    @Autowired
    JuegoService juegoService;

    @PostMapping(value = "/reglas", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public String setReglas(@RequestBody ReglaDeJuego payload) throws IOException {

        juegoService.setReglas(payload);

        return JSONHelper.successMessage("recibido");
    }

    @GetMapping(value = "/jugar", produces = {MediaType.TEXT_HTML_VALUE})
    public void jugar(HttpServletResponse response, @RequestParam String c) throws IOException {

        response.sendRedirect(juegoService.resultado(c)+"?c="+c);
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

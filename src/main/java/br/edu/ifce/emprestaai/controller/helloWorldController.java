package br.edu.ifce.emprestaai.controller;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/hello-world")
public class helloWorldController {

    @GetMapping
    public String getHelloWorld() {
        return "hello world";
    }
}


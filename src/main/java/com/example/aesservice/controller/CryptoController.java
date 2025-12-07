package com.example.aesservice.controller;

import com.example.aesservice.dto.CryptoRequest;
import com.example.aesservice.dto.CryptoResponse;
import com.example.aesservice.service.CryptoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CryptoController {

    private final CryptoService cryptoService;

    public CryptoController(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    @PostMapping("/encrypt")
    public ResponseEntity<CryptoResponse> encrypt(@RequestBody CryptoRequest request) {
        String cipherBase64 = cryptoService.encrypt(request.getText(), request.getKey());
        return ResponseEntity.ok(new CryptoResponse(cipherBase64));
    }

    @PostMapping("/decrypt")
    public ResponseEntity<CryptoResponse> decrypt(@RequestBody CryptoRequest request) {
        String plain = cryptoService.decrypt(request.getText(), request.getKey());
        return ResponseEntity.ok(new CryptoResponse(plain));
    }
}

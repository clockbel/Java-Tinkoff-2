package edu.java.controller;

import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TgChatApiController implements TgChatApi {
    @Override
    public ResponseEntity<Void> tgChatIdPost(@Min(1) @PathVariable Long id) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> tgChatIdDelete(@Min(1) @PathVariable Long id) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

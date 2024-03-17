package edu.java.bot.controller;

import edu.java.models.request.LinkUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdateController implements UpdateApi {
    @Override
    public void sendUpdate(@Valid @RequestBody LinkUpdateRequest linkUpdateRequest) {
    }
}

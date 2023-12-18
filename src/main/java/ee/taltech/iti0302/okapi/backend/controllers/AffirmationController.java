package ee.taltech.iti0302.okapi.backend.controllers;

import ee.taltech.iti0302.okapi.backend.services.AffirmationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/public/affirmation")
public class AffirmationController {
    private final AffirmationService affirmationService;

    @GetMapping
    public String getAffirmation() {
        return affirmationService.getAffirmation();
    }
}

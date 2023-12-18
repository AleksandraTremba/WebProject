package ee.taltech.iti0302.okapi.backend.services;

import ee.taltech.iti0302.okapi.backend.dto.affirmation.AffirmationDTO;
import ee.taltech.iti0302.okapi.backend.entities.Affirmation;
import ee.taltech.iti0302.okapi.backend.repository.AffirmationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class AffirmationService {
    private final AffirmationRepository affirmationRepository;
    private Long affirmationDataId = 1L;
    private final RestTemplate restTemplate = new RestTemplate();
    private LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Scheduled(fixedDelay = 30000)
    public void requestAffirmation() {
        AffirmationDTO response = restTemplate.getForObject("https://www.affirmations.dev", AffirmationDTO.class);
        if (response == null) {
            log.info(getCurrentTime() + ": affirmation response was null");
            return;
        }

        Affirmation affirmation = affirmationRepository.findById(affirmationDataId).orElse(null);
        if (affirmation == null) {
            affirmation = new Affirmation();
            affirmationDataId = affirmation.getId();
            log.info(getCurrentTime() + ": new affrimation has been made");
        }

        affirmation.setAffirmation(response.getAffirmation());
        affirmationRepository.save(affirmation);
        log.info(getCurrentTime() + ": Affirmation has been updated with affirmation: {}", response.getAffirmation());
    }

    public String getAffirmation() {
        Affirmation aff = affirmationRepository.findById(1L).orElse(null);
        if (aff == null)
            return "Unfortunately no affirmation was found :(( ";

        return aff.getAffirmation();
    }
}

package be.vdab.cinefest.medewerkers;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
class MedewerkerService {
    private final MedewerkerRepository medewerkerRepository;

    public MedewerkerService(MedewerkerRepository medewerkerRepository) {
        this.medewerkerRepository = medewerkerRepository;
    }
    List<Medewerker> findByStukVoornaamEnStukFamilienaam(String stukvoornaam, String stukfamilienaam){
        return medewerkerRepository.findByStukVoornaamEnStukFamilienaam(stukvoornaam, stukfamilienaam);
    }
}

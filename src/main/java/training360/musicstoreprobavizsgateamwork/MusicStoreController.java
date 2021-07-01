package training360.musicstoreprobavizsgateamwork;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/instruments")
public class MusicStoreController {

    private MusicStoreService musicStoreService;

    public MusicStoreController(MusicStoreService musicStoreService) {
        this.musicStoreService = musicStoreService;
    }

    @GetMapping()
    public List<InstrumentDTO> getInstruments(@RequestParam Optional<String> brand, @RequestParam Optional<Integer> price) {
        return musicStoreService.getInstruments(brand,price);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public InstrumentDTO addInstrument(@Valid @RequestBody CreateInstrumentCommand command) {
        return musicStoreService.addInstrument(command);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAll() {
        musicStoreService.deleteAll();
    }

    @GetMapping("/{id}")
    public InstrumentDTO findInstrumentById(@PathVariable Long id) {
        return musicStoreService.findInstrumentById(id);
    }

    @PutMapping("/{id}")
    public InstrumentDTO updatePrice(@PathVariable Long id, @RequestBody UpdatePriceCommand command) {
        return musicStoreService.updatePrice(id, command);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInstrument(@PathVariable Long id) {
        musicStoreService.deleteInstrument(id);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Problem> handleNotFound(IllegalArgumentException exception) {
        Problem problem = Problem.builder()
                .withType(URI.create("instruments/not-found"))
                .withTitle("Not found")
                .withStatus(Status.NOT_FOUND)
                .withDetail(exception.getMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Problem> handleValidException(MethodArgumentNotValidException exception) {
        List<Violation> violations =
                exception.getBindingResult().getFieldErrors().stream()
                        .map(fe -> new Violation(fe.getField(), fe.getDefaultMessage()))
                        .collect(Collectors.toList());

        Problem problem = Problem.builder()
                .withType(URI.create("locations/validation-error"))
                .withTitle("Validation error")
                .withStatus(Status.BAD_REQUEST)
                .withDetail(exception.getMessage())
                .with("violation", violations)
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);

    }

}

//Ebben a feladatban egy hangszeráruház online webshopalkalmazás backend részét kell megvalósítanod.
//
//Az alap entitás az Instrument melynek van egy egyedi azonosítója, egy márkája, egy típusa, egy ára, és egy közzététel dátuma. Kritériumok:
//
//A típus enum legyen, melyben a következő értékek lehetnek : ELECTRIC_GUITAR, ACOUSTIC_GUITAR, PIANO
//A közzététel dátuma LocalDate
//Valósítsd meg a MusicStoreServie osztályt, mely egy listában tárolja a hangszereket. Ez a lista kezdetben üres.
// Ez az osztály felelős az id kiosztásért is amikor új elem érkezik.
//
//A MusicController osztálynak a következő funkciókat kell megvalósítania:
//
//Alapértelmezetten a /api/instruments URL-n várjuk a kéréseket
//
//Az alapértelmezett URL-n lehessen az összes hangszert lekérdezni. Itt opcionáliasan lehessen márkát és/vagy árat megadni.
// Ilyenkor csak a lekérdezett márkájú, vagy árú vagy a kérésnek megfelelően mindkét tulajdonsággal rendelkező elemek jelenjenek meg
//
//Az alapértelmezett URL-n keresztül lehessen új hangszert felvenni. Ekkor csak a márkát, típust és árat várjuk a dátumot az aznapi dátumra állítsuk be.
//
//Az alapértelmezett URL-n keresztül lehessen törölni az összes hangszert
//
//A /{id} URL-n keresztül lehessen lekérdezni egy hangszert. Figyeljünk arra, hogyha nem megfelelő id-t kapunk akkor 404, not found státusszal térjünk vissza
//
//A /{id} URL-n keresztül lehessen frissíteni az árat. Ha az ár ugyanaz mint amit már tárolunk akkor ne történjen semmi,
// ha az ár más, akkor az árat és a dátumot is frissítsük!
//
//A /{id} URL-n keresztül lehessen törölni az aktuális elemet
//
//További kritériumok:
//
//Ne lehessen létrehozni elemet meg nem adott márkával és negatív árral
//Ne lehessen frissíteni az árat negatív árral
//Figyeljünk, hogy a tesztnek megfelelő kritériumokat teljesítsük. (url, státusz-kód, stb)
//Jó munkát!
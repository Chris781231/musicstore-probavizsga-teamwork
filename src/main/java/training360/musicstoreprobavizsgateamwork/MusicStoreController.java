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

//Ebben a feladatban egy hangszer??ruh??z online webshopalkalmaz??s backend r??sz??t kell megval??s??tanod.
//
//Az alap entit??s az Instrument melynek van egy egyedi azonos??t??ja, egy m??rk??ja, egy t??pusa, egy ??ra, ??s egy k??zz??t??tel d??tuma. Krit??riumok:
//
//A t??pus enum legyen, melyben a k??vetkez?? ??rt??kek lehetnek : ELECTRIC_GUITAR, ACOUSTIC_GUITAR, PIANO
//A k??zz??t??tel d??tuma LocalDate
//Val??s??tsd meg a MusicStoreServie oszt??lyt, mely egy list??ban t??rolja a hangszereket. Ez a lista kezdetben ??res.
// Ez az oszt??ly felel??s az id kioszt??s??rt is amikor ??j elem ??rkezik.
//
//A MusicController oszt??lynak a k??vetkez?? funkci??kat kell megval??s??tania:
//
//Alap??rtelmezetten a /api/instruments URL-n v??rjuk a k??r??seket
//
//Az alap??rtelmezett URL-n lehessen az ??sszes hangszert lek??rdezni. Itt opcion??liasan lehessen m??rk??t ??s/vagy ??rat megadni.
// Ilyenkor csak a lek??rdezett m??rk??j??, vagy ??r?? vagy a k??r??snek megfelel??en mindk??t tulajdons??ggal rendelkez?? elemek jelenjenek meg
//
//Az alap??rtelmezett URL-n kereszt??l lehessen ??j hangszert felvenni. Ekkor csak a m??rk??t, t??pust ??s ??rat v??rjuk a d??tumot az aznapi d??tumra ??ll??tsuk be.
//
//Az alap??rtelmezett URL-n kereszt??l lehessen t??r??lni az ??sszes hangszert
//
//A /{id} URL-n kereszt??l lehessen lek??rdezni egy hangszert. Figyelj??nk arra, hogyha nem megfelel?? id-t kapunk akkor 404, not found st??tusszal t??rj??nk vissza
//
//A /{id} URL-n kereszt??l lehessen friss??teni az ??rat. Ha az ??r ugyanaz mint amit m??r t??rolunk akkor ne t??rt??njen semmi,
// ha az ??r m??s, akkor az ??rat ??s a d??tumot is friss??ts??k!
//
//A /{id} URL-n kereszt??l lehessen t??r??lni az aktu??lis elemet
//
//Tov??bbi krit??riumok:
//
//Ne lehessen l??trehozni elemet meg nem adott m??rk??val ??s negat??v ??rral
//Ne lehessen friss??teni az ??rat negat??v ??rral
//Figyelj??nk, hogy a tesztnek megfelel?? krit??riumokat teljes??ts??k. (url, st??tusz-k??d, stb)
//J?? munk??t!
package training360.musicstoreprobavizsgateamwork;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Instrument {
    private Long id;
    private String brand;
    private InstrumentType type;
    private int price;
    private LocalDate postDate;
}

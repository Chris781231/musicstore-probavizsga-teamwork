package training360.musicstoreprobavizsgateamwork;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateInstrumentCommand {

    @NotBlank()
    private String brand;

    private InstrumentType type;

    @Positive
    private int price;
}

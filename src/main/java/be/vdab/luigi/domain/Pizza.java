package be.vdab.luigi.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public class Pizza {
    @PositiveOrZero
    private final long id;
    @NotBlank
    private final String naam;
    @PositiveOrZero
    private final BigDecimal prijs;
    private final boolean pikant;

    public Pizza(long id, String naam, BigDecimal prijs, boolean pikant) {
        this.id = id;
        this.naam = naam;
        this.prijs = prijs;
        this.pikant = pikant;
    }

    public long getId() {
        return id;
    }

    public String getNaam() {
        return naam;
    }

    public BigDecimal getPrijs() {
        return prijs;
    }

    public boolean isPikant() {
        return pikant;
    }
}

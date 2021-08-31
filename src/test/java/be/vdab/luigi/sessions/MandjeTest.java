package be.vdab.luigi.sessions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MandjeTest {
    private Mandje mandje;
    @BeforeEach
    void beforeEach(){
        mandje = new Mandje();
    }
    @Test
    void eenNieuwMandjeIsLeeg(){
        assertThat(mandje.getIds()).isEmpty();
    }
    @Test
    void mandjeBevatItemDatIsToegevoegd(){
        mandje.voegToe(10L);
        assertThat(mandje.getIds()).containsOnly(10L);
    }
    @Test
    void itemKanMaar1KeerWordenToegevoegd(){
        mandje.voegToe(10L);
        mandje.voegToe(10L);
        assertThat(mandje.getIds()).containsOnly(10L);
    }
    @Test
    void tweeItemsToevoegen(){
        mandje.voegToe(10L);
        mandje.voegToe(20L);
        assertThat(mandje.getIds()).containsOnly(10L,20L);
    }
}
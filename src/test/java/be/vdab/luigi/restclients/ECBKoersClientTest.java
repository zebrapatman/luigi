package be.vdab.luigi.restclients;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import be.vdab.luigi.exception.KoersClientException;
import be.vdab.luigi.restclients.ECBKoersClient;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@PropertySource("application.properties")
@Import(ECBKoersClient.class)
class ECBKoersClientTest {
    private final ECBKoersClient client;

    ECBKoersClientTest(ECBKoersClient client) {
        this.client = client;
    }

    @Test
    void getDollarKoers() {
        assertThat(client.getDollarKoers()).isPositive();
    }
}
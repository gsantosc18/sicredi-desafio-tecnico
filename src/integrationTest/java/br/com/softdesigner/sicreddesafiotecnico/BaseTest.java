package br.com.softdesigner.sicreddesafiotecnico;

import br.com.softdesigner.sicreddesafiotecnico.document.PautaDocument;
import br.com.softdesigner.sicreddesafiotecnico.document.SessaoDocument;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public abstract class BaseTest {
    public static final String PAUTA_ID = "123456789";
    public static final String PAUTA_NAME = "Pauta test";
    public static final String SESSION_ID = "123456";

    protected PautaDocument getPautaDocument() {
        return new PautaDocument(PAUTA_ID, PAUTA_NAME);
    }

    protected SessaoDocument getSessaoDocument(Long minutes) {
        return new SessaoDocument(SESSION_ID,getPautaDocument(), getTimePlusMinutes(minutes));
    }

    protected LocalDateTime getTimePlusMinutes(Long minutes) {
        return LocalDateTime.of(2022,4,8,0,0,0)
                .plusMinutes(minutes);
    }
}

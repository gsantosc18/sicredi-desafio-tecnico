package br.com.softdesigner.sicreddesafiotecnico;

import br.com.softdesigner.sicreddesafiotecnico.document.AssociadoDocument;
import br.com.softdesigner.sicreddesafiotecnico.document.PautaDocument;
import br.com.softdesigner.sicreddesafiotecnico.document.SessaoDocument;
import br.com.softdesigner.sicreddesafiotecnico.document.VotoDocument;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static br.com.softdesigner.sicreddesafiotecnico.enums.VotoEnum.SIM;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public abstract class BaseTest {
    public static final String PAUTA_ID = "123456789";
    public static final String PAUTA_NAME = "Pauta test";
    public static final String SESSION_ID = "123456";
    public static final String CPF = "01102203344";
    public static final String ASSOCIADO_ID = "123456";

    protected PautaDocument getPautaDocument() {
        return new PautaDocument(PAUTA_ID, PAUTA_NAME);
    }

    protected SessaoDocument getSessaoDocument(Long minutes) {
        return new SessaoDocument(SESSION_ID,getPautaDocument(), getTimePlusMinutes(minutes));
    }

    protected SessaoDocument getSessaoDocumentInvalid() {
        LocalDateTime timeExpired = LocalDateTime.now().minusDays(1);
        return new SessaoDocument(SESSION_ID,getPautaDocument(), timeExpired);
    }

    protected LocalDateTime getTimePlusMinutes(Long minutes) {
        return LocalDateTime.now().withNano(0).plusMinutes(minutes);
    }

    protected VotoDocument getVotoDocument() {
        return new VotoDocument(getAssociadoDocument(), getSessaoDocument(120L), SIM);
    }

    protected AssociadoDocument getAssociadoDocument() {
        return new AssociadoDocument(ASSOCIADO_ID, CPF);
    }
}

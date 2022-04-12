package br.com.softdesigner.sicreddesafiotecnico;

import br.com.softdesigner.sicreddesafiotecnico.document.AssociadoDocument;
import br.com.softdesigner.sicreddesafiotecnico.document.PautaDocument;
import br.com.softdesigner.sicreddesafiotecnico.document.SessaoDocument;
import br.com.softdesigner.sicreddesafiotecnico.document.VotoDocument;
import br.com.softdesigner.sicreddesafiotecnico.dto.CreateVotoDTO;
import br.com.softdesigner.sicreddesafiotecnico.dto.UserStatusDTO;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static br.com.softdesigner.sicreddesafiotecnico.enums.UserStatusEnum.ABLE_TO_VOTE;
import static br.com.softdesigner.sicreddesafiotecnico.enums.UserStatusEnum.UNABLE_TO_VOTE;
import static br.com.softdesigner.sicreddesafiotecnico.enums.VotoEnum.SIM;

@ExtendWith(SpringExtension.class)
public abstract class BaseTest {
    protected static final String CPF = "01102203344";
    public static final String SESSION_ID = "123456789";
    public static final String PAUTA_ID = "12345679";
    public static final String PAUTA_NAME = "Pauta teste";
    public static final int SESSAO_MINUTE_DEFAULT = 120;
    public static final String ASSOCIADO_ID = "11223344";

    protected CreateVotoDTO createVotoDTO() {
        return new CreateVotoDTO(SESSION_ID, CPF, SIM);
    }

    protected UserStatusDTO getUserStatusUnableToVote() {
        return new UserStatusDTO(UNABLE_TO_VOTE);
    }

    protected UserStatusDTO getUserStatusAbleToVote() {
        return new UserStatusDTO(ABLE_TO_VOTE);
    }

    protected SessaoDocument getSessaoTimeInvalid() {
        final LocalDateTime timeAfterNow = LocalDateTime.now().minusHours(1);
        return new SessaoDocument(SESSION_ID, getPautaDocument(), timeAfterNow);
    }

    protected SessaoDocument getSessao() {
        final LocalDateTime timeAfterNow = LocalDateTime.now().plusMinutes(SESSAO_MINUTE_DEFAULT);
        return new SessaoDocument(SESSION_ID, getPautaDocument(), timeAfterNow);
    }

    protected PautaDocument getPautaDocument() {
        return new PautaDocument(PAUTA_ID, PAUTA_NAME);
    }

    protected AssociadoDocument getAssociadoDocument() {
        return new AssociadoDocument(ASSOCIADO_ID, CPF);
    }

    protected VotoDocument getVotoDocument() {
        return new VotoDocument(getAssociadoDocument(),getSessao(),SIM);
    }
}

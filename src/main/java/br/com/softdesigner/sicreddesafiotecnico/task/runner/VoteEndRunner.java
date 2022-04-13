package br.com.softdesigner.sicreddesafiotecnico.task.runner;

import br.com.softdesigner.sicreddesafiotecnico.document.SessaoDocument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class VoteEndRunner implements Runnable{
    private SessaoDocument sessaoDocument;

    @Override
    public void run() {
        log.info("M=run, message=Fim da sessão");
    }

    public void setSessaoDocument(SessaoDocument sessao) {
        this.sessaoDocument = sessao;
    }
}

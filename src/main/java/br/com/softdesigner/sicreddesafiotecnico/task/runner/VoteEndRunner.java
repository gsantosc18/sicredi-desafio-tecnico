package br.com.softdesigner.sicreddesafiotecnico.task.runner;

import br.com.softdesigner.sicreddesafiotecnico.document.SessaoDocument;
import org.springframework.stereotype.Component;

@Component
public class VoteEndRunner implements Runnable{
    private SessaoDocument sessaoDocument;

    @Override
    public void run() {
    }

    public void setSessaoDocument(SessaoDocument sessao) {
        this.sessaoDocument = sessao;
    }
}

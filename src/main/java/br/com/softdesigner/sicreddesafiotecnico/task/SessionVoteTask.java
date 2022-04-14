package br.com.softdesigner.sicreddesafiotecnico.task;

import br.com.softdesigner.sicreddesafiotecnico.document.SessaoDocument;
import br.com.softdesigner.sicreddesafiotecnico.task.runner.VoteEndRunner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class SessionVoteTask {
    private final TaskScheduler taskScheduler;
    private final VoteEndRunner voteEndRunner;

    public void schedule(String sessaoId, Date dateTime) {
        log.info("M=createSessao, message=Agendado o fim da sessão, time={}", dateTime);
        voteEndRunner.setSessaoId(sessaoId);
        taskScheduler.schedule(voteEndRunner,dateTime);
    }
}

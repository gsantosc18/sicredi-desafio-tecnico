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

    public void schedule(SessaoDocument sessaoDocument, Date dateTime) {
        voteEndRunner.setSessaoDocument(sessaoDocument);
        taskScheduler.schedule(voteEndRunner,dateTime);
    }
}

package br.com.softdesigner.sicreddesafiotecnico.converter;

import br.com.softdesigner.sicreddesafiotecnico.document.PautaDocument;
import org.springframework.beans.BeanUtils;

public class Pauta {
    public static PautaDocument fromDto(Object o) {
        PautaDocument pautaDocument = new PautaDocument();
        BeanUtils.copyProperties(o, pautaDocument);
        return pautaDocument;
    }

    public static <T> T toDto(PautaDocument pautaDocument) {
        T dto = (T) new Object();
        BeanUtils.copyProperties(pautaDocument, dto);
        return dto;
    }
}

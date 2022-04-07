package br.com.softdesigner.sicreddesafiotecnico.converter;

import br.com.softdesigner.sicreddesafiotecnico.document.PautaDocument;
import br.com.softdesigner.sicreddesafiotecnico.dto.PautaDTO;
import org.springframework.beans.BeanUtils;

public class PautaConverter {
    public static PautaDTO toDto(PautaDocument pautaDocument) {
        PautaDTO pautaDTO = new PautaDTO();
        BeanUtils.copyProperties(pautaDocument, pautaDTO);
        return pautaDTO;
    }
}

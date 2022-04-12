package br.com.softdesigner.sicreddesafiotecnico.converter;

import br.com.softdesigner.sicreddesafiotecnico.document.AssociadoDocument;
import br.com.softdesigner.sicreddesafiotecnico.dto.AssociadoDTO;
import org.springframework.beans.BeanUtils;

public class AssociadoConverter {
    public static AssociadoDTO toDto(AssociadoDocument associadoDocument) {
        AssociadoDTO associadoDTO = new AssociadoDTO();
        BeanUtils.copyProperties(associadoDocument, associadoDTO);
        return associadoDTO;
    }
}

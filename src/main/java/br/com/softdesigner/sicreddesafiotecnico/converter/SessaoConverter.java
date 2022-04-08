package br.com.softdesigner.sicreddesafiotecnico.converter;

import br.com.softdesigner.sicreddesafiotecnico.document.SessaoDocument;
import br.com.softdesigner.sicreddesafiotecnico.dto.PautaDTO;
import br.com.softdesigner.sicreddesafiotecnico.dto.SessaoDTO;
import org.springframework.beans.BeanUtils;

public class SessaoConverter {
    public static SessaoDTO toDto(SessaoDocument sessaoDocument) {
        SessaoDTO sessaoDTO = new SessaoDTO();
        PautaDTO pautaDTO = PautaConverter.toDto(sessaoDocument.getPauta());
        BeanUtils.copyProperties(sessaoDocument,sessaoDTO);
        sessaoDTO.setPauta(pautaDTO);
        return sessaoDTO;
    }
}

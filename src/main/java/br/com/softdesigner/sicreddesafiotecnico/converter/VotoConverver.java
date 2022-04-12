package br.com.softdesigner.sicreddesafiotecnico.converter;

import br.com.softdesigner.sicreddesafiotecnico.document.VotoDocument;
import br.com.softdesigner.sicreddesafiotecnico.dto.VotoDTO;

public class VotoConverver {
    public static VotoDTO toDTO(VotoDocument votoDocument) {
        VotoDTO votoDTO = new VotoDTO();
        votoDTO.setVoto(votoDocument.getVoto());
        votoDTO.setAssociado(AssociadoConverter.toDto(votoDocument.getAssociado()));
        votoDTO.setSessao(SessaoConverter.toDto(votoDocument.getSessao()));
        return votoDTO;
    }
}

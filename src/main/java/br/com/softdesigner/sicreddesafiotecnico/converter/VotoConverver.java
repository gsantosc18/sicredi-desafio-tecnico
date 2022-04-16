package br.com.softdesigner.sicreddesafiotecnico.converter;

import br.com.softdesigner.sicreddesafiotecnico.document.VotoDocument;
import br.com.softdesigner.sicreddesafiotecnico.dto.VotoDTO;
import br.com.softdesigner.sicreddesafiotecnico.enums.VotoEnum;

public class VotoConverver {
    public static VotoDTO toDTO(VotoDocument votoDocument) {
        VotoDTO votoDTO = new VotoDTO();
        VotoEnum voto = VotoEnum.valueOf(votoDocument.getVoto());
        votoDTO.setVoto(voto);
        votoDTO.setAssociado(AssociadoConverter.toDto(votoDocument.getAssociado()));
        votoDTO.setSessao(SessaoConverter.toDto(votoDocument.getSessao()));
        return votoDTO;
    }
}

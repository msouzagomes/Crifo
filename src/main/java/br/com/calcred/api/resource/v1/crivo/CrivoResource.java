package br.com.calcred.api.resource.v1.crivo;

import br.com.calcred.api.integration.crivo.dto.CrivoConsultaLimiteRequestDTO;
import br.com.calcred.api.integration.crivo.dto.CrivoConsultaLimiteResponseDTO;
import br.com.calcred.api.service.CrivoService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/")
public class CrivoResource {

    private final CrivoService crivoService;

    @PostMapping(value = "/consultaLimite" )
    public CrivoConsultaLimiteResponseDTO crivoConsultaLimite(@RequestBody @Validated final CrivoConsultaLimiteRequestDTO crivoConsultaLimiteRequestDTO) {
        return crivoService.crivoConsultaLimite(crivoConsultaLimiteRequestDTO);
    }
}

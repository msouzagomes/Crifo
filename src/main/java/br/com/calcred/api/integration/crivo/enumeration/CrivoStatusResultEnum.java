package br.com.calcred.api.integration.crivo.enumeration;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CrivoStatusResultEnum {

    APROVADA(1, "APROVADA", "A"),
    NEGADA(2, "NEGADA", "R"),
    PENDENTE(3, "PENDENTE", "P");

    private final Integer id;
    private final String code;
    private final String situation;

    public static CrivoStatusResultEnum doDeparaRespostaCrivo(String status) {
        return switch (status) {
            case "A" -> CrivoStatusResultEnum.APROVADA;
            case "R" -> CrivoStatusResultEnum.NEGADA;
            default -> CrivoStatusResultEnum.PENDENTE;
        };
    }
}


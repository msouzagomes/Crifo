package br.com.calcred.api.integration.crivo.enumeration;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CrivoPolicyEnum {

    EMPRESTIMO_PESSOAL("Calcred_Concessao__P1");

    private final String value;

}

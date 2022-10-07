package br.com.calcred.api.integration.crivo.enumeration;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CrivoCriterionEnum {

    LIMITE_SUGERIDO("limite_sugerido"),
    RC_RENDA_PRESUMIDA("Serasa_RendaPresumida_P1_v3__Calcred"),
    POLITICA_EMPRETIMO_PESSOAL("Calcred_Concessao__P1");

    private final String code;

}

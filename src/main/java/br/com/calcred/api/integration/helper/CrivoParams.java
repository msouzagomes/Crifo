package br.com.calcred.api.integration.helper;

public class CrivoParams {

    public enum PersonalLoan {
        CPF("CPF"),
        CODIGO_DA_LOJA_IN("Código_da_Loja_IN"),
        CANAL("Canal");

        private String value;

        PersonalLoan(String value){
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }
}

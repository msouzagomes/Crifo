package br.com.calcred.api.integration.crivo.dto;

import br.com.calcred.api.integration.crivo.enumeration.CrivoPolicyEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;

public class ParametersDTO implements Serializable {

    private static final long serialVersionUID = 1654764484814053271L;
    private static final Locale PT_BR = new Locale("pt", "BR");
    private static final String NEW_LINE = "\n";
    private static final String SEMICOLON = ";";
    private static final String PARAMETER_PREFIX = "P";

    private CrivoPolicyEnum policy;
    private Map<String, Object> parameters;

    public ParametersDTO() {
        parameters = new HashMap<>();
    }

    public ParametersDTO(String key, Object value) {
        parameters = new HashMap<>();
        parameters.put(key, value);
    }

    public CrivoPolicyEnum getPolicy() {
        return policy;
    }

    public void setPolicy(CrivoPolicyEnum policy) {
        this.policy = policy;
    }

    public ParametersDTO addParameter(String key, Object value) {
        parameters.put(key, value);
        return this;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public String parseToString() {
        StringBuilder parametersPrefix = new StringBuilder(PARAMETER_PREFIX);
        parametersPrefix.append(NEW_LINE);
        NumberFormat nf = NumberFormat.getInstance(PT_BR);
        SortedSet<String> keys = new TreeSet<>(this.parameters.keySet());
        for (String key : keys) {
            Object value = this.parameters.get(key);
            if (value == null) {
                value = "";
            } else if (value instanceof BigDecimal) {
                BigDecimal bd = (BigDecimal) value;
                value = nf.format(bd);
            }
            parametersPrefix.append(key).append(SEMICOLON).append(value).append(NEW_LINE);
        }
        return parametersPrefix.toString();
    }

    public ParametersDTO incrementParameters(ParametersDTO parametersDTO) {
        this.parameters.putAll(parametersDTO.getParameters());
        return this;
    }
}

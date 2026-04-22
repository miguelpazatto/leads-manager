package com.miguelpazatto.leadsmanager.entities.enums;

public enum LeadClassification {

	HOT(
			1, 
			0, 
			40, 
			"Performance Crítica",
			"Sua operação e rotina estão em zona de risco. O desequilíbrio entre CNPJ e CPF está drenando sua escala e gerando estagnação",
			"HOT", 
			"Lead sobrecarregado. Focar em 'alívio imediato' e 'recuperação de tempo'. Abordagem urgente"
			),
	
	WARM(
			2,
			41,
			70,
			"Performance em Alerta",
			"Você já tem tração, mas existem 'gargalos silenciosos' impedindo o próximo nível. É necessário ajustar processos para não bater no teto.",
			"WARM",
			"Lead que fatura mas não tem liberdade. Focar em 'processos' e 'delegação'. Venda consultiva."
			),
	
	COLD(
			3,
			71,
			100,
			"Alta Performance",
			"Seus indicadores mostram maturidade acima da média. O foco agora é blindagem de rotina e escala exponencial do ecossistema.",
			"COLD",
			"Lead de alto nível. Abordagem sênior. Focar em 'exclusividade', 'networking' e 'otimização fina'."
			);
	
	private final int code;
	private final int min;
	private final int max;
	private final String titleLead;
	private final String messageLead;
	private final String titleSales;
	private final String messageSales;
	
	
	private LeadClassification(int code, int min, int max, String titleLead, String messageLead, String titleSales, String messageSales) {
		this.code = code;
		this.min = min;
		this.max = max;
		this.titleLead = titleLead;
		this.messageLead = messageLead;
		this.titleSales = titleSales;
		this.messageSales = messageSales;	
	}
	
	public int getCode() {
		return code;
	}
	
	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}

	public String getTitleLead() {
		return titleLead;
	}

	public String getMessageLead() {
		return messageLead;
	}

	public String getTitleSales() {
		return titleSales;
	}

	public String getMessageSales() {
		return messageSales;
	}

	public static LeadClassification valueOf(int code) {
		for (LeadClassification value : LeadClassification.values()) {
			if (code == value.getCode()) {
				return value;
			}
		}
		throw new IllegalArgumentException("Invalid LeadClassification code");
	}
	
}

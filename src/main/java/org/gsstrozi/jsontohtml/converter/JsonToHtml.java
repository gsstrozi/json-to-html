package org.gsstrozi.jsontohtml.converter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.GsonBuilder;

public class JsonToHtml {
	private final Stack stack = new Stack();

	public String convert(String json) throws IOException {
		this.stack.reset();
		if (json == null) {
			return "" + json;
		}
		LinkedHashMap<?, ?> map = new GsonBuilder().create().fromJson(json, LinkedHashMap.class);
		criarEstruturaHTML(map);
		return this.stack.getValue();
	}

	private void parseKey(Object key) {
		this.stack.push("" + key);
	}

	private void criarEstruturaHTML(Object value) {
		this.stack.push("<div class=\"col-sm-12\">");
		if ((value instanceof Map)) {
			Map<?, ?> object = (Map<?, ?>) value;
			criarTabelaHTML(object, object);
		} else if ((value instanceof List)) {
			List<?> objectList = (List<?>) value;
			criarListaAgrupadaHTML(objectList);
		} else {
			this.stack.push(formatarValorColuna(value));
		}
		this.stack.push("</div>");
	}

	private void criarTabelaHTML(Map<?, ?> mapaCompleto, Map<?, ?> items) {
		for (Map.Entry<?, ?> entry : items.entrySet()) {
			if ((mapaCompleto.containsKey(entry.getKey())) || ((entry.getValue() instanceof Map))
					|| ((entry.getValue() instanceof List))) {
				criarLabelHTML(entry.getKey());
			}
			if ((entry.getValue() instanceof Map)) {
				Map<?, ?> object = (Map<?, ?>) entry.getValue();
				criarTabelaHTML(mapaCompleto, object);
			} else if ((entry.getValue() instanceof List)) {
				List<?> objectList = (List<?>) entry.getValue();
				criarListaAgrupadaHTML(objectList);
			} else {
				this.stack.push("<table class=\"table table-bordered\">");
				this.stack.push("<tr>");
				criarCelulaTabelaHTML(entry);
				this.stack.push("</tr>");
				this.stack.push("</table>");
			}
		}
	}

	private void criarLabelHTML(Object keyValue) {
		String groupLabel = criarChaveParaGrupo(keyValue);

		this.stack.push("<label class=\"text-uppercase\" style=\"font-size: 12px\">");
		parseKey(groupLabel);
		this.stack.push("</label>");
	}

	private void criarCelulaTabelaHTML(Map.Entry<?, ?> entry) {
		if (entry.getValue() != null) {
			criarLinhaTabela(entry);
		} else {
			this.stack.push("&nbsp;");
		}
	}

	private void criarLinhaTabela(Map.Entry<?, ?> entry) {
		this.stack.push("<td width=\"20%\">");
		parseKey(criarChaveParaGrupo(entry.getKey()));
		this.stack.push("</td>");
		this.stack.push("<td>");
		criarEstruturaHTML(entry.getValue());
		this.stack.push("</td>");
	}

	private void criarListaAgrupadaHTML(List<?> items) {
		this.stack.push("<table class=\"table table-bordered\">");
		for (int i = 0; i < items.size(); i++) {
			criarItensListaHTML(items.get(i), i == 0);
		}
		this.stack.push("</table>");
	}

	private void criarItensListaHTML(Object item, boolean makeHeader) {
		if ((item instanceof Map)) {
			if (makeHeader) {
				criarCabecalhoListaHTML((Map<?, ?>) item);
			}
			criarLinhaListaHTML((Map<?, ?>) item);
		} else if (item != null) {
			this.stack.push("" + item);
		}
	}

	private void criarCabecalhoListaHTML(Map<?, ?> map) {
		this.stack.push("<tr>");
		for (Map.Entry<?, ?> entry : map.entrySet()) {
			if (!(entry.getValue() instanceof List)) {
				this.stack.push("<th>");
				this.stack.push("<label class=\"text-uppercase\">");
				this.stack.push(criarChaveParaGrupo(entry.getKey()));
				this.stack.push("</label>");
				this.stack.push("</th>");
			}
		}
		this.stack.push("</tr>");
	}

	private void criarLinhaListaHTML(Map<?, ?> map) {
		this.stack.push("<tr>");
		for (Map.Entry<?, ?> entry : map.entrySet()) {
			if ((entry.getValue() instanceof List)) {
				criarListaAgrupadaHTML((List<?>) entry.getValue());
			} else {
				this.stack.push("<td>");
				this.stack.push(formatarValorColuna(entry.getValue()));
				this.stack.push("</td>");
			}
		}
		this.stack.push("</tr>");
	}

	private String formatarValorColuna(Object valorCelula) {
		String celulaFormatada = "&nbsp;";
		if (valorCelula != null) {
			celulaFormatada = valorCelula.toString();
		}
		return celulaFormatada;
	}

	private String criarChaveParaGrupo(Object chave) {
		String nomeGrupo = "" + chave;
		return nomeGrupo.replaceAll("_", " ").replaceAll("-", " ").replaceAll("null", "");
	}

	public String buildError() {
		this.stack.reset();

		this.stack.push("<div class=\"col-sm-12\">");
		this.stack.push("<label class=\"text-uppercase\" style=\"font-size: 12px\">");
		this.stack.push("JSON INVÁLIDO");
		this.stack.push("</label>");
		this.stack.push("</div>");

		return this.stack.getValue();
	}
}

package org.gsstrozi.jsontohtml.controller;

import org.gsstrozi.jsontohtml.converter.JsonToHtml;
import org.gsstrozi.jsontohtml.util.FileTemplateUtil;
import org.jboss.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JsonToHtmlController {
	private static final Logger LOG = Logger.getLogger(JsonToHtmlController.class);

	@RequestMapping(path = "/json", produces = MediaType.APPLICATION_XHTML_XML_VALUE, method = RequestMethod.POST)
	public ResponseEntity<String> helper(@RequestBody(required = true) String json) {
		JsonToHtml bodyHTML = new JsonToHtml();
		try {
			StringBuilder htmlTemplate = FileTemplateUtil.getTemplateFile();
			String body = bodyHTML.convert(json);

			return ResponseEntity.status(HttpStatus.OK)
					.body(FileTemplateUtil.replaceBody(htmlTemplate.toString(), body));
		} catch (Exception e) {
			LOG.error("Erro ao processar JSON", e);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bodyHTML.buildError());
	}
}

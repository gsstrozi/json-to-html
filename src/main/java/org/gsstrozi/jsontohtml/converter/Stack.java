package org.gsstrozi.jsontohtml.converter;

class Stack {
	private final StringBuilder text = new StringBuilder();
	private int level = 0;

	void reset() {
		this.text.setLength(0);
	}

	void push(String token) {
		if (isClosingTag(token)) {
			this.level -= 1;
		}
		this.text.append(getIndentation(this.level));
		this.text.append(token);
		if (isOpeningTag(token)) {
			this.level += 1;
		}
		this.text.append("\n");
	}

	String getValue() {
		return this.text.toString();
	}

	private boolean isOpeningTag(String token) {
		return (token.startsWith("<")) && (!token.startsWith("</")) && (token.endsWith(">"));
	}

	private boolean isClosingTag(String token) {
		return (token.startsWith("</")) && (token.endsWith(">"));
	}

	private String getIndentation(int level) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < level; i++) {
			sb.append("    ");
		}
		return sb.toString();
	}
}

package com.devsuperior.dscatalog.Resources.exceptions;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError {

	private List<FieldMenssage> erros = new ArrayList<>();

	public List<FieldMenssage> getErros() {
		return erros;
	}
	
	public void addError(String fieldName, String message) {
		erros.add(new FieldMenssage(fieldName, message));
	}
}

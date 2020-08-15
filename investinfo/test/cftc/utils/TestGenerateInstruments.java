package cftc.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class TestGenerateInstruments {

	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		GenerateInstruments.generateInstrumentsFromJson();
	}

}

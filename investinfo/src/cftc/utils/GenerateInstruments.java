package cftc.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GenerateInstruments {

	public static void generateInstrumentsFromJson() throws JsonParseException, JsonMappingException, IOException {
		
		ObjectMapper mapper = new ObjectMapper();

		System.out.println("Read instruments from " + Constants.INSTRUMENTS_FILE_PATH);
		
		Map<String, List<String>> instruments = mapper.readValue(
				new File(Constants.INSTRUMENTS_FILE_PATH), 
				new TypeReference<Map<String, List<String>>>() {
		});
		
		generateInstrumentsModel(instruments);
	}

	private static void generateInstrumentsModel(Map<String, List<String>> instruments) throws FileNotFoundException, IOException {
		//remove file:///
		String path = (Constants.GENERATED_FOLDER_PATH + "/cftc/model/instrument").substring(8);
		File file = new File(path, "InstrumentName.java");
		file.createNewFile();
		
		try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
		    writer.println("package cftc.model.instrument;");
		    writer.println();
		    writer.println("public enum InstrumentName {");
		    writer.println();
		    
		    StringBuilder sb = new StringBuilder();
		    for (String category : instruments.keySet()) {
		    	 List<String> list = instruments.get(category);
		    		 
		    	 for (String instrument : list) {
		    		 sb.append("\t").append(instrument).append(",").append("\n");
		    	 }
		    }
		    
		    sb = sb.deleteCharAt(sb.length() - 2);
		    
		    writer.println(sb);
		    
		    writer.println("}");
		} 
		
	}
}

package ejercicio13;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


public class Programa {
	
	public static void main(String[] args) {

		List<String> pokemones = Arrays.asList("charizard", "mewtwo", "grovyle", "gengar");
		String rutaSalida = "src\\ejercicio13\\salidaPokemon.txt";
		try {
			
		
		File archivoSalida = new File(rutaSalida);
		if (!archivoSalida.exists()) {
			archivoSalida.createNewFile();
		}
		FileWriter fw = new FileWriter(archivoSalida);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write("PROGRAMA DE POKEMON\n");
		for(String poke: pokemones) {
			try {
				URL endpoint = new URL("https://pokeapi.co/api/v2/pokemon/"+poke);
				HttpURLConnection con = (HttpURLConnection) endpoint.openConnection();
				con.connect(); 
				int tiempoDeRespuesta = con.getResponseCode();
				if (tiempoDeRespuesta != 200)
				{
					throw new RuntimeException("HttpResponseCode" + tiempoDeRespuesta);			
				}
				else
				{
					InputStreamReader input = new InputStreamReader(con.getInputStream());
					
					
					Gson datag = new Gson();
					JsonElement datae = datag.fromJson(input, JsonElement.class);
					JsonObject data = datae.getAsJsonObject();
					String nombre = data.get("species").getAsJsonObject().get("name").getAsString();
					List<JsonElement> tipoList = data.get("types").getAsJsonArray().asList();
					String tipos = tipoList.get(0).getAsJsonObject().get("type").getAsJsonObject().get("name").getAsString();
					for(int i=1;i<tipoList.size();i++) {
						tipos+= " - "+tipoList.get(i).getAsJsonObject().get("type").getAsJsonObject().get("name").getAsString();
					}
					String peso = data.get("weight").getAsString();
					
					System.out.println(nombre);
					System.out.println(peso);
					System.out.println(tipos);
					System.out.println("--------------------------------------------------");
					
					bw.write("\nNombre del pokemon: "+nombre);
					bw.write("\nPeso: "+peso);
					bw.write("\nTipos: "+tipos);
					bw.write("\n--------------------------------------------------");
					
					
				}
			} catch (Exception e) {
				System.out.println("Oops! hubo una excepción");
			}
		}
		bw.close();
		System.out.println("Programa finalizado con exito, tenga un buen dia");
		}
		catch(IOException ioe) {
			System.out.println("Oops! hubo una excepción con el archivo");
		}
	}
}

package com.sprite.concurso.helper;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;


public final class MarshallHelper {

	private MarshallHelper() {
		throw new IllegalStateException("Utility MarshallHelper");
	}

	/**
	 * 
	 * @param booking
	 * @return
	 * @throws JsonProcessingException
	 */
	public static String toJson(Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (JsonProcessingException e) {
		}

		return null;
	}

	/**
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static <T> T fromJson(String jsonStr, Class<T> cls) throws IOException {
		return new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(jsonStr,
				cls);
	}

	/**
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static List<Map<String, Object>> fromJson(String jsonStr) throws IOException {

		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(jsonStr, new TypeReference<List<Map<String, Object>>>() {
		});
	}

	/**
	 * 
	 * @param jsonStr
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static Map<String, Object> toMap(String jsonStr) throws IOException {
		return new ObjectMapper().readValue(jsonStr, new TypeReference<Map<String, Object>>() {
		});
	}

	/**
	 * 
	 * @param obj
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> toMap(Object obj) {
		return new ObjectMapper().convertValue(obj, Map.class);
	}

	/**
	 * 
	 * @param obj
	 * @return
	 * @throws MarshallHelperException
	 * @throws JAXBException
	 * @throws IOException
	 */
	public static <T> String marshall(T obj) {
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(obj.getClass());
			final StringWriter writer = new StringWriter();
			context.createMarshaller().marshal(obj, writer);
			String response = writer.toString();
			writer.close();

			return response;
		} catch (JAXBException | IOException e) {

		}

		return null;

	}

	/**
	 * 
	 * @param message
	 * @return
	 * @throws JAXBException
	 */
	public static <T> T unmarshall(Class<T> cls, String message) throws JAXBException {
		final JAXBContext jaxbContext = JAXBContext.newInstance(cls);
		final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		final StringReader strReader = new StringReader(message);
		final StreamSource strSource = new StreamSource(strReader);
		final JAXBElement<T> jfp = unmarshaller.unmarshal(strSource, cls);
		return jfp.getValue();
	}
}

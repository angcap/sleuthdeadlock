package online.pelago.p4p.shipitinerary.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.opencsv.ICSVWriter;
import com.opencsv.bean.BeanVerifier;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.MappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvConstraintViolationException;

import online.pelago.p4p.shipitinerary.exceptions.BusinessRequirementViolationException;

/**
 * CSV file parser interface with a default implementation based on {@link CsvToBeanBuilder}
 * with <code>UTF_8</code> encoding and <code>IgnoreLeadingWhiteSpace</code> setting enabled.
 * @see http://opencsv.sourceforge.net/#processors_and_validators
 */
public interface CSVParser<T> {
	/**
	 * 
	 * @return a default implementation that return always <code>true</code>: every bean is valid!
	 * 	 */
	default BeanVerifier<T> getBeanVerifier() {
		return new BeanVerifier<T>() {

			@Override
			public boolean verifyBean(Object bean) throws CsvConstraintViolationException {
				return true;
			}
			
		};
	}
	
	/**
	 * Uses {@link #getBeanVerifier()} as BeanVerifier to validate row beans
	 * @param content
	 * @param clazz
	 * @param mappingStrategy
	 * @param separator
	 * @return
	 * @throws IOException
	 * @throws BusinessRequirementViolationException
	 */
	default List<T> parse(byte[] content, Class<T> clazz, MappingStrategy<T> mappingStrategy, char separator)
			throws IOException, BusinessRequirementViolationException {

		try (InputStreamReader csvStreamReader = new InputStreamReader(new ByteArrayInputStream(content),
				StandardCharsets.UTF_8)) {
			CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(csvStreamReader)
					.withType(clazz)
					.withMappingStrategy(mappingStrategy)
					.withIgnoreLeadingWhiteSpace(true)
					.withSeparator(separator)
					.withVerifier(getBeanVerifier())
					.build();
			return csvToBean.parse();
		} catch (Exception ex) {
			throw new BusinessRequirementViolationException(ex.getMessage()
					+ (ex.getCause() != null ? " " + ex.getCause().getMessage() : ""));
		}
	}
	
	default void format(PrintWriter writer, List<T> entitiesList, MappingStrategy<T> mappingStrategy, char separator)
			throws BusinessRequirementViolationException {
		try {
			StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(writer)
					.withMappingStrategy(mappingStrategy)
					.withQuotechar(ICSVWriter.NO_QUOTE_CHARACTER)
					.withSeparator(separator)
					.build();
			beanToCsv.write(entitiesList);
			writer.close();
		} catch (Exception ex) {			
			throw new BusinessRequirementViolationException(ex.getMessage()
					+ (ex.getCause() != null ? " " + ex.getCause().getMessage() : ""));
		}
	}
}

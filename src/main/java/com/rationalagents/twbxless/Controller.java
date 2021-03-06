package com.rationalagents.twbxless;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

@RestController
public class Controller {

	private final HyperService hyperService;

	public Controller(HyperService hyperService) {
		this.hyperService = hyperService;
	}

	@RequestMapping(value="filenames", method = RequestMethod.GET, produces="text/plain")
	public String getFilenames(@RequestParam String url) {
		return Csv.toCsv("filenames", hyperService.getFilenames(url));
	}

	@RequestMapping(value="data", method = RequestMethod.GET, produces="text/plain")
	public String getData(@RequestParam String url, @RequestParam String filename) {

		try {
			return Csv.toCsv(hyperService.getData(url, filename));
		} catch (DataException e) {
			return Csv.toCsv(e);
		}
	}

	/**
	 * Nicer here would be message converter but planning to do something else
	 */
	private static class Csv {
		static String toCsv(String singleHeader, List<String> singleColumn) {
			List<List<String>> list = new ArrayList<>();
			list.add(List.of(singleHeader));
			singleColumn.forEach(v -> list.add(List.of(v)));
			return toCsv(list);
		}

		static String toCsv(DataException e) {
			return toCsv(e.getMessage(), e.getExtraData());
		}

		static String toCsv(List<List<String>> rows) {
			StringWriter writer = new StringWriter();
			CsvListWriter csvWriter = new CsvListWriter(writer, CsvPreference.STANDARD_PREFERENCE);

			try {
				for (List<String> row : rows) {
					csvWriter.write(row);
				}
				csvWriter.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			return writer.toString();
		}
	}
}

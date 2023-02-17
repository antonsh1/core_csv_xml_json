package ru.smartjava;

import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;
import ru.smartjava.classes.Converter;
import ru.smartjava.classes.Employee;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

        final String ROOT_PATH = "src\\main\\resources\\";
        String csvFileName = ROOT_PATH + "data.csv";
        String xmlFileName = ROOT_PATH + "data.xml";
        String jsonFileName1 = ROOT_PATH + "data.json";
        String jsonFileName2 = ROOT_PATH + "dataFromXml.json";
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};

        Converter converter = new Converter();
        System.out.println("------- CSV - JSON парсер --------");
        System.out.println("Файл CSV " + csvFileName);
        List<Employee> employeeFromCsvList = converter.parseCSV(columnMapping,csvFileName);
        System.out.println(employeeFromCsvList);
        String jsonString = converter.listToJson(employeeFromCsvList);
        System.out.println(jsonString);
        System.out.println("Файл JSON " + jsonFileName1);
        converter.writeStringToFile(jsonFileName1, jsonString);
        System.out.println("---------------------------------------");

        System.out.println("-------- XML - JSON парсер -------");
        System.out.println("Файл XML " + xmlFileName);
        List<Employee> employeeFromXmlList = converter.parseXML(xmlFileName, columnMapping);
        System.out.println(employeeFromXmlList);
        jsonString = converter.listToJson(employeeFromCsvList);
        System.out.println(jsonString);
        System.out.println("Файл JSON " + jsonFileName2);
        converter.writeStringToFile(jsonFileName2, jsonString);
        System.out.println("---------------------------------------");

        System.out.println("---------- JSON парсер ---------");
        System.out.println("Файл XML " + jsonFileName2);
        String jsonString2 = converter.readString(jsonFileName2);
        System.out.println(jsonString2);
        List<Employee> employeeFromJsonList = converter.jsonToList(jsonString2, columnMapping);
        System.out.println(employeeFromJsonList);
        System.out.println("---------------------------------------");
    }


}
package ru.smartjava.classes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Converter {

    public List<Employee> jsonToList(String jsonString, String[] columnMapping)  {
        List<Employee> employeeList = new ArrayList<>();
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(jsonString);
            JSONArray jsonArray = (JSONArray) obj;
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Employee employee = new Employee();
                for(String item : columnMapping) {
                    mapToEmployee(employee, item, jsonObject.get(item).toString());
                }
                employeeList.add(employee);
            }
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return employeeList;
    }

    private void mapToEmployee(Employee employee, String name, String value) {
            switch (name) {
                case "id":
                    employee.id = Long.parseLong(value);
                    break;
                case "firstName":
                    employee.firstName = value;
                    break;
                case "lastName":
                    employee.lastName = value;
                    break;
                case "country":
                    employee.country = value;
                    break;
                case "age":
                    employee.age = Integer.parseInt(value);
                    break;
        }
    }
    public String readString(String filename) {
        String line;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
            line = bufferedReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return line;
    }

    public List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> list = new ArrayList<>();
        ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
        strategy.setType(Employee.class);
        strategy.setColumnMapping(columnMapping);
        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(fileName))
                .build()) {
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            list = csv.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Employee> parseXML(String fileName, String[] columnMapping) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(fileName));
        Node root = doc.getDocumentElement();
        return read(root, columnMapping);
    }

    private List<Employee> read(Node node, String[] columnMapping) {
        NodeList nodeList = node.getChildNodes();
        List<Employee> employeeList = new ArrayList<>();
        String[] listAttributes = {};
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node_ = nodeList.item(i);
            if (Node.ELEMENT_NODE == node_.getNodeType()) {
                if (node_.getNodeName().equals("employee")) {
                    NodeList subNodeList = node_.getChildNodes();
                    Employee employee = new Employee();
                    for (int a = 0; a < subNodeList.getLength(); a++) {
                        Node subNodeList_ = subNodeList.item(a);
                        if (Arrays.asList(columnMapping).contains(subNodeList_.getNodeName())) {
                            String value = subNodeList_.getTextContent();
                            mapToEmployee(employee, subNodeList_.getNodeName(), value);
                        }
                    }
                    employeeList.add(employee);
                }
            }
        }
        return employeeList;
    }

    public String listToJson(List<Employee> employeeList) {
        Gson gson = new GsonBuilder().create();
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        return gson.toJson(employeeList, listType);
    }

    public void writeStringToFile(String fileName, String anyString) {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(anyString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

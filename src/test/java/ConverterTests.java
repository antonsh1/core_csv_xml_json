import org.hamcrest.collection.IsEmptyCollection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;
import ru.smartjava.classes.Converter;
import ru.smartjava.classes.Employee;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;


public class ConverterTests {
    Converter converter;
    String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
    List<Employee> employeeList = new ArrayList<>() {
        {
            add(new Employee(1, "FIRST", "FIRST", "USA", 25));
            add(new Employee(2, "SECOND", "SECOND", "RU", 23));
        }
    };

    @BeforeEach
    public void beforeEach() {
        this.converter = new Converter();
    }

    @AfterEach
    public void AfterEach() {
        this.converter = null;
    }

    @Test
    public void ParseXMLTest() throws ParserConfigurationException, IOException, SAXException {

        //arrange
        String fileName = "src/test/resources/testdata.xml";

        //act
        List<Employee> result = converter.parseXML(fileName, columnMapping);
        List<Employee> emptyResult = new ArrayList<>();

        //assert
        Assertions.assertEquals(result, employeeList);

        //используя hamcrest
        assertThat(result, not(IsEmptyCollection.empty()));
        //пустая коллекция вызывает ошибку
        //assertThat(emptyResult,not(IsEmptyCollection.empty()));

    }

    //полностью hamcrestб различные варианты
    @Test
    public void MatchersParseXMLTest() throws ParserConfigurationException, IOException, SAXException {

        String fileName = "src/test/resources/testdata.xml";

        List<Employee> result = converter.parseXML(fileName, columnMapping);

        assertThat(result.get(0), isA(Employee.class));
        assertThat(result, notNullValue());
        assertThat(result, anything());
        assertThat(result, anyOf(anything(), notNullValue()));
        assertThat(result, allOf(anything(), notNullValue()));

    }

    @Test
    public void listToJson() {
        //arrange
        String expectedResult = "[{\"id\":1,\"firstName\":\"FIRST\",\"lastName\":\"FIRST\",\"country\":\"USA\",\"age\":25},{\"id\":2,\"firstName\":\"SECOND\",\"lastName\":\"SECOND\",\"country\":\"RU\",\"age\":23}]";

        //act
        String result = converter.listToJson(employeeList);

        //assert
        Assertions.assertEquals(result, expectedResult);

    }

    @Test
    public void parseCSV() {
        //arrange
        String fileName = "src/test/resources/testdata.csv";
        //act
        List<Employee> result = converter.parseCSV(columnMapping, fileName);
        //assert
        Assertions.assertEquals(result, employeeList);

    }
}

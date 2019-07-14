package by.touchsoft.vasilyevanatali.clientAction;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;


public class ConsoleReaderTest {

    @Test
    public void readFromConsole() throws IOException {
        String testString = "test\nstring";
        InputStream stream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));


        Assert.assertEquals("test", reader.readLine());
        Assert.assertEquals("string", reader.readLine());

    }
}
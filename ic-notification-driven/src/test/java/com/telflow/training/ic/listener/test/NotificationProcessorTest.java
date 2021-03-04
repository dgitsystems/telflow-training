package com.telflow.training.ic.listener.test;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.junit.Test;

/**
 * @author macbookpro2
 *
 */
public class NotificationProcessorTest {

    @Test
    public void test() throws JAXBException {
        try {
            File file = new File("/Users/macbookpro/Documents/binotification.txt");

            file.createNewFile();
            
            //BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
            //writer.write(biNotification);
            
            //writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

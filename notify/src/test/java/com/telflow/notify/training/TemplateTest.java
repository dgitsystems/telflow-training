package com.telflow.notify.training;

import biz.dgit.schemas.telflow.cim.v3.NotificationProperty;
import biz.dgit.schemas.telflow.cim.v3.OperationNotification;
import com.telflow.fabric.test.FabricTestHelper;
import com.telflow.notify.plugins.artefact.template.TemplateArtefactPlugin;
import com.telflow.notify.plugins.artefact.template.preprocessor.ClassNamePreprocessor;
import com.telflow.notify.plugins.artefact.template.preprocessor.ContractValuePreProcessor;
import com.telflow.notify.plugins.artefact.template.preprocessor.DescribedByPreprocessor;
import com.telflow.notify.plugins.artefact.template.preprocessor.NotesPreProcessor;
import com.telflow.notify.plugins.artefact.template.preprocessor.NotificationPropertyPreProcessor;
import com.telflow.notify.plugins.artefact.template.preprocessor.PreProcessor;
import com.telflow.notify.plugins.artefact.template.preprocessor.TaskSnoozePreprocessor;
import com.telflow.notify.plugins.artefact.template.preprocessor.TimeFormatPreprocessor;
import com.telflow.notify.plugins.artefact.template.preprocessor.TypesPreProcessor;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * A test class for running templates
 */
public class TemplateTest {

    private static final String ACTUAL_TEMPLATE_FILE_PATH = "target/out.html";

    private TemplateArtefactPlugin plugin;

    private List<NotificationProperty> property;

    @Before
    public void setUp() {
        List<PreProcessor> preProcessors = new ArrayList<>();
        preProcessors.add(new NotesPreProcessor());
        preProcessors.add(new ClassNamePreprocessor());
        preProcessors.add(new DescribedByPreprocessor());
        preProcessors.add(new TimeFormatPreprocessor());
        preProcessors.add(new TypesPreProcessor());
        preProcessors.add(new NotificationPropertyPreProcessor());
        preProcessors.add(new ContractValuePreProcessor());
        preProcessors.add(new TaskSnoozePreprocessor());

        this.plugin = new TemplateArtefactPlugin(
                "src/main/etc/templates",
                preProcessors
        );

        this.property = Arrays.asList(new NotificationProperty().withKey("templateName")
                .withValue("requestBodyTemplate.peb"));
    }

    @Test
    public void testTransformCustomerOrderNotification() throws IOException {
        runTest("customer-order-notification.xml");
    }

    private void runTest(String notificationFilePath) throws IOException {
        OperationNotification notification = FabricTestHelper.createCimObjectFromFile(
            notificationFilePath, OperationNotification.class);

        byte[] artefact = this.plugin.buildArtefact(notification, null, this.property);
        FileUtils.writeByteArrayToFile(new File(ACTUAL_TEMPLATE_FILE_PATH), artefact);
        File actualArtefact = FileUtils.getFile(ACTUAL_TEMPLATE_FILE_PATH);
        Assert.assertNotNull(actualArtefact);
    }
}
package com.jo.application.util;

import com.vaadin.flow.component.UI;
import org.hashids.Hashids;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
/**
 * Utility class for generating and opening zoom URLs using table and record IDs.
 * The class fetches properties from the application context and generates hash-based URLs
 * to be opened in new browser tabs.
 * <p>
 * Implements {@link ApplicationContextAware} to access the Spring {@link Environment} for retrieving properties.
 * </p>
 */
public class ZoomUtil implements ApplicationContextAware {

    private static Environment environment;

    /**
     * Sets the {@link ApplicationContext} to obtain the {@link Environment} for property access.
     *
     * @param context the {@link ApplicationContext} instance used to set the environment
     * @throws BeansException if there is an issue setting the application context
     */
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        environment = context.getEnvironment();
    }

    /**
     * Retrieves a property value from the environment based on the given key.
     *
     * @param key the property key to retrieve
     * @return the property value corresponding to the given key, or {@code null} if not found
     */
    public static String getProperty(String key) {
        return environment.getProperty(key);
    }

    /**
     * Generates a zoom URL using the specified table ID and record ID, and opens the URL in a new browser tab.
     * The table ID and record ID are hashed to create a secure URL format.
     *
     * @param tableID the ID of the table for which the URL is generated
     * @param recordID the ID of the record for which the URL is generated
     */
    public static void generateAndOpenUrl(int tableID, int recordID) {
        // Fetch values from application.properties using Environment
        String fullUrl = generateURL(tableID, recordID);

        // Open the generated URL in a new tab
        UI.getCurrent().getPage().executeJs("window.open($0, '_blank');", fullUrl);
    }

    /**
     * Generates a hashed URL using the given table ID and record ID.
     * The URL format is constructed as "?table={tableHash}&id={recordHash}" where the IDs are hashed.
     * <p>
     * The method uses the {@code ampere.application.url} and {@code ampere.application.hashid}
     * properties to build the base URL and perform the hashing operation.
     * </p>
     *
     * @param tableID the ID of the table to be hashed and included in the URL
     * @param recordID the ID of the record to be hashed and included in the URL
     * @return the generated URL containing hashed table and record IDs
     */
    public static String generateURL(int tableID, int recordID) {
        String applicationUrl = ZoomUtil.getProperty("ampere.application.url");
        String hashid = ZoomUtil.getProperty("ampere.application.hashid");

        // Create a Hashids instance using the salt from the property
        Hashids hashids = new Hashids(hashid);

        // Generate the encoded URL
        String tableHash = hashids.encode(tableID);
        String recordHash = hashids.encode(recordID);
        return applicationUrl + "?table=" + tableHash + "&id=" + recordHash;
    }
}


package com.example.config;/* in case using minio from docker-compose
package com.com.example.config;

import io.minio.MinioClient;
import jakarta.enterprise.inject.Produces;
import org.jboss.logging.Logger;

public class MinioClientFactory {

    private static final Logger LOGGER = Logger.getLogger(MinioClientFactory.class);
    private final MinioConfig minIOConfig;

    public MinioClientFactory(MinioConfig minIOConfig) {
        this.minIOConfig = minIOConfig;
    }

    @Produces
    public MinioClient minioClient() {
        try {
            return MinioClient.builder()
                    .endpoint(
                            minIOConfig.host(),
                            minIOConfig.port(),
                            minIOConfig.useSsl())
                    .credentials(
                            minIOConfig.accessKey(),
                            minIOConfig.secretKey())
                    .build();
        } catch (Exception e) {
            LOGGER.errorf(e, "Error while initializing minioClient");
            throw new RuntimeException(e);
        }
    }
}
*/

package com.example.controller;

import com.example.service.LogDataService;
import com.example.service.MinioService;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

@Path("/api/v1/file-sharing-bot")
public class BotController {
    private static final Logger LOGGER = Logger.getLogger(BotController.class);
    private final MinioService minioService;
    private final LogDataService logDataService;

    public BotController(MinioService minioService, LogDataService logDataService) {
        this.minioService = minioService;
        this.logDataService = logDataService;
    }

    @GET
    @Path("/download")
    @Produces("application/zip")
    public Response downloadZip(@NotNull @QueryParam("chatId") Long chatId) {
        LOGGER.infof("Attempt to download in %s", chatId);
        byte[] bytes = minioService.downloadZip(String.valueOf(chatId));
        logDataService.save(chatId);
        return Response.ok(bytes)
                .header("Content-Disposition", "attachment; filename=file_sharing_bot.zip")
                .build();
    }
}
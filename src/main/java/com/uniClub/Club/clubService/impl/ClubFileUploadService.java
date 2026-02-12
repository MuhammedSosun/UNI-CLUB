package com.uniClub.Club.clubService.impl;

import com.uniClub.file.BaseFileUploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ClubFileUploadService extends BaseFileUploadService {
    @Value("${app.config.upload-dir}")
    private String uploadDir;

    @Override protected String getTargetDirectory() { return uploadDir; }
    @Override protected String getPrefix() { return "club"; }
}
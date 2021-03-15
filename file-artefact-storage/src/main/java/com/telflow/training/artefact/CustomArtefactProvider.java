package com.telflow.training.artefact;

import java.io.InputStream;
import java.util.List;

import com.telflow.artefact.api.ArtefactstoreProvider;

public class CustomArtefactProvider implements ArtefactstoreProvider {

    @Override
    public String getProviderId() {
        return "CustomArtefactStore";
    }

    @Override
    public InputStream read(String path) {
        // Read file operation
        return null;
    }

    @Override
    public List<String> readDir(String path) {
        // Read Directory operation
        return null;
    }

    @Override
    public boolean unlink(String path) {
        // Unlink / Delete file
        return true;
    }

    @Override
    public boolean write(String fullPath, byte[] data, String contentType) {
        // Write file
        return true;
    }

}

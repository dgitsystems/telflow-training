package com.telflow.training.artefact;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telflow.artefact.api.ArtefactstoreProvider;

/**
 * This class detects artefact and store them to into specified path in file system
 *
 */
public class FileProvider implements ArtefactstoreProvider {
    
    private static final transient Logger LOG = LoggerFactory.getLogger(FileProvider.class);
    
    private String fileSystemPath;

    public String getProviderId() {
        return "TrainingFileArtefactStore";
    }

    /**
     * Read and download file content from specified file system path
     * @param path temp path
     * @return file content
     */
    public InputStream read(String path) {
        String fileName = stripFilenameFromPath(path);
        File newFile = new File(this.fileSystemPath + fileName);
        
        try {
            byte[] bytes = FileUtils.readFileToByteArray(newFile);
            return new ByteArrayInputStream(bytes);
        } catch (IOException e) {
            LOG.error("Unable to read file {} from file : {}", fileName, e.getMessage());
            throw new RuntimeException("Unable to read file from file system");
        }
        
    }

    public List<String> readDir(String path) {
        return null;
    }

    /**
     * Delete file content from specified file system path
     * @param path temp path
     * @return success status
     */
    public boolean unlink(String path) {
        String fileName = stripFilenameFromPath(path);
        
        File newFile = new File(this.fileSystemPath + fileName);
        
        try {
            FileUtils.forceDelete(newFile);
            return true;
        } catch (IOException e) {
            LOG.error("Unable to delete file {} from file system : {}", fileName, e.getMessage());
            return false;
        }
    }

    /**
     * Write file into specified folder
     * 
     * @param fullPath temp path
     * @param data content in byte
     * @param contentType content type
     * @return created status
     */
    public boolean write(String fullPath, byte[] data, String contentType) {
        String fileName = stripFilenameFromPath(fullPath);
        
        File newFile = new File(this.fileSystemPath + fileName);
        
        LOG.info("Storing file to : " + this.fileSystemPath + fileName);
        
        try {
            FileUtils.writeByteArrayToFile(newFile, data, false);
            return true;
        } catch (IOException e) {
            LOG.error("Unable to write file {} to file system : {}", fileName, e.getMessage());
            return false;
        }
    }
    
    private String stripFilenameFromPath(String path) {
        String fileName = path.substring(path.lastIndexOf('-') + 1);

        return fileName;
    }

    public void setFileSystemPath(String fileSystemPath) {
        this.fileSystemPath = fileSystemPath;
    }

}

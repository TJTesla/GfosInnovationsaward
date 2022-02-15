package gfos.controller;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.*;

@Named
@RequestScoped
public class uploadtestController implements Serializable {
    private UploadedFile file;
    private StreamedContent downloadFile;

    public void upload() {
        if (file == null) {
            return;
        }

        String name = file.getFileName();
        System.out.println("Uploaded file: " + name);

        String path = "uploads/" + name;
        // Create empty file with current working directory
        File tempFile = new File(System.getProperty("user.dir"));

        // Create file with parent directory of current working directory
        File newFile = new File(path);
        try {
            InputStream in = file.getInputStream();
            OutputStream out = new FileOutputStream(newFile);

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
        } catch (IOException ioException) {
            System.out.println("Error while uploading file: " + ioException.getMessage());
        }

        System.out.println(newFile.getAbsolutePath());
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public StreamedContent getDownloadFile() throws FileNotFoundException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream("uploads/UT.pdf"));

        downloadFile = DefaultStreamedContent.builder()
                .name("download_UT.pdf")
                .contentType("application/pdf")
                .stream(() -> bis)
                .build();

        return downloadFile;
    }

    public void setDownloadFile(StreamedContent downloadFile) {
        this.downloadFile = downloadFile;
    }

    public String dummyAction() {
        System.out.println("Uploaded File Name Is :: " + file.getFileName() + " :: Uploaded File Size :: " + file.getSize());
        return "";
    }

    public void fileUploadListener(FileUploadEvent e) {
        // Get uploaded file from the FileUploadEvent
        this.file = e.getFile();
        // Print out the information of the file
        System.out.println("Uploaded File Name Is :: " + file.getFileName() + " :: Uploaded File Size :: " + file.getSize());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("File uploaded successfully."));
    }
}

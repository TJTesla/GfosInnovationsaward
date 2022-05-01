package gfos.pojos;

import gfos.exceptions.UploadException;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

import java.io.*;

// Statische Klasse zum hoch und runterladen von Dateien

public class ResourceIO {

    public static Resume uploadResume(UploadedFile file, Offer offer, User user) throws UploadException, IOException {
        if (file == null || isIncorrectFiletype(file.getFileName(), new String[]{"pdf"}) || file.getFileName().contains("/")) {
            throw new UploadException("The uploaded resume file may only be a PDF and mustn't contain a '/' character");
        }

        String path = "uploads/resumes/" + user.getName() + "/" + offer.getId() + file.getFileName();
        Resume result = new Resume(-1, path, file.getFileName());

        // Gewährleisten, dass der Benutzerordner zum Speichern der Lebensläufe existiert
        File directory = new File(System.getProperty("user.dir"), "uploads/resumes/" + user.getName());
        directory.mkdirs();

        // Schon bestehende Datei löschen
        deleteFile(path);
        save(file, new File(System.getProperty("user.dir"), path));

        return result;
    }

    public static void deleteFile(String path) {
        File f = new File(System.getProperty("user.dir"), path);
        f.delete();
    }

    // Überprüfen, welche Dateiendung die Datei hat
    // Abgleichen mit den Typen im Parameter filytypes
    private static boolean isIncorrectFiletype(String str, String[] filetypes) {
        if (str.length() < 5) {
            return true;
        }
        String filetype = str.substring(str.length()-3).toLowerCase();

        for (String type : filetypes) {
            if (filetype.equals(type)) {
                return false;
            }
        }
        return true;
    }

    // Hochladen von Profilbildern
    public static String uploadPb(UploadedFile file, User user) throws UploadException, IOException {
        if (file == null || isIncorrectFiletype(file.getFileName(), new String[]{"png", "jpg"}) || file.getFileName().contains("/")) {
            throw new UploadException("The uploaded file must be an allowed file type and mustn't contain a '/' character");
        }

        // Falls Bewerber schon eins hat -> löschen
        Applicant applicant = (Applicant)user;
        if (applicant.getPb() != null && !applicant.getPb().isEmpty()) {
            new File(applicant.getPb()).delete();
        }

        String folder = "";
        folder = "applicants";
        String path = "uploads/profilepics/" + folder + "/" + user.getName() + file.getFileName();
        File f = new File(System.getProperty("user.dir"), path);

        // Gewährleisten, dass Ordnerstrukur besteht
        File parent = f.getParentFile();
        parent.mkdirs();

        save(file, f);

        return path;
    }

    // In das Dateisystem schreiben
    private static void save(UploadedFile file, File saveFile) throws IOException {
        InputStream is = file.getInputStream();
        OutputStream os = new FileOutputStream(saveFile);

        int read = 0;
        byte[] bytes = new byte[1024];

        while ((read = is.read(bytes)) != -1) {
            os.write(bytes, 0, read);
        }
    }

    // Übergeordnete Verzeichnis der übergebenen Datei löschen
    public static void deleteUserDir(String filePath) {
        File file = new File(System.getProperty("user.dir"), filePath);
        File dir = file.getParentFile();
        dir.delete();
    }
}

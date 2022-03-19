package gfos.beans;

import gfos.exceptions.UploadException;
import gfos.longerBeans.CurrentUser;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

import javax.inject.Inject;
import java.io.*;


public class ResourceIO {

    public static Resume uploadResume(UploadedFile file, Offer offer, User user) throws UploadException, IOException {
        if (file == null || isIncorrectFiletype(file.getFileName(), new String[]{"pdf"}) || file.getFileName().contains("/")) {
            throw new UploadException("The uploaded resume file may only be a PDF and mustn't contain a '/' character");
        }

        String path = "uploads/resumes/" + user.getName() + "/" + offer.getId() + file.getFileName();
        Resume result = new Resume(path, file.getFileName());

        File directory = new File(System.getProperty("user.dir"), "uploads/resumes/" + user.getName());
        directory.mkdirs();

        save(file, new File(System.getProperty("user.dir"), path));

        return result;
    }

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

    public static String uploadPb(UploadedFile file, User user) throws UploadException, IOException {
        if (file == null || isIncorrectFiletype(file.getFileName(), new String[]{"png", "jpg"}) || file.getFileName().contains("/")) {
            throw new UploadException("The uploaded file must be an allowed file type and mustn't contain a '/' character");
        }

        String folder = "";
        if (user instanceof Applicant) {
            folder = "applicants";
        } /*else if (user instanceof Company) {
            folder = "copanies";
        }*/
        String path = "uploads/profilepics/" + folder + "/" + user.getName() + file.getFileName();

        save(file, new File(System.getProperty("user.dir"), path));

        return path;
    }

    private static void save(UploadedFile file, File saveFile) throws IOException {
        InputStream is = file.getInputStream();
        OutputStream os = new FileOutputStream(saveFile);

        int read = 0;
        byte[] bytes = new byte[1024];

        while ((read = is.read(bytes)) != -1) {
            os.write(bytes, 0, read);
        }
    }

    public StreamedContent downloadPb(User user) {
        return null;
    }

    public StreamedContent downloadResume(Applicant applicant, Offer offer) {
        return null;
    }
}

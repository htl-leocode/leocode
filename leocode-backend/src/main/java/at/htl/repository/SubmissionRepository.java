package at.htl.repository;

import at.htl.control.GitController;
import at.htl.dto.SubmissionDTO;
import at.htl.entity.Submission;
import io.quarkus.hibernate.orm.panache.Panache;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.apache.commons.io.IOUtils;
import org.eclipse.jgit.api.Git;
import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.MultivaluedMap;
import java.io.*;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@ApplicationScoped
public class SubmissionRepository implements PanacheRepository<Submission> {

    @Inject
    Logger logger;

    @Transactional
    public void update(Submission s){
        Panache.getEntityManager().merge(s);
    }

    @Transactional
    public List<Submission> getFinishedByUsername(String username) {
        return find("select s from Submission s where author = ?1 and result is not NULL", username).list();
    }

    public List<SubmissionDTO> createSubmissionDTOs(List<Submission> submissionList) {
        List<SubmissionDTO> submissionDTOs = new LinkedList<>();
        submissionList.forEach(submission -> {
            submissionDTOs.add(new SubmissionDTO(submission));
        });
        return submissionDTOs;
    }

    public String createSubmissionZip(Submission submission, List<InputPart> codeFiles){


        String path = "../projects-ziped/" + submission.example.name +"-"+submission.id+".zip";

        new File(path).getParentFile().mkdirs();

        try (FileOutputStream fos = new FileOutputStream(path);
             ZipOutputStream zos = new ZipOutputStream(fos)){

            for (InputPart part: codeFiles) {
                MultivaluedMap<String, String> header = part.getHeaders();
                String fileName = header.toString().split("filename=\"")[1].split("\"")[0];
                byte[] bytes = part.getBody(byte[].class, null);

                zos.putNextEntry(new ZipEntry(fileName));
                zos.write(bytes, 0, bytes.length);

                zos.closeEntry();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "";
    }


    public String copyFilesToTestDir(Submission submission, List<InputPart> codeFiles) {
        String path = String.format("../../project-under-test/%s/project-under-test-%s/",submission.example.type,submission.id);
        logger.info("Copying files to "+path);

        //new File(path).getParentFile().mkdirs();
        new File(path).mkdirs();

        for (InputPart inputPart : codeFiles) {
            try {

                MultivaluedMap<String, String> header = inputPart.getHeaders();
                var fileName = getFileName(header);

                //convert the uploaded file to inputstream
                InputStream inputStream = inputPart.getBody(InputStream.class,null);

                byte [] bytes = IOUtils.toByteArray(inputStream);

                //constructs upload file path
                fileName = path + fileName;

                writeFile(bytes,fileName);

                System.out.println("Done");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return path;
    }

    private String getFileName(MultivaluedMap<String, String> header) {

        String finalFileName = getString(header);
        if (finalFileName != null) return finalFileName;
        return "unknown";
    }

    static String getString(MultivaluedMap<String, String> header) {
        String[] contentDisposition = header.getFirst("Content-Disposition").split(";");

        for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("filename"))) {

                String[] name = filename.split("=");

                String finalFileName = name[1].trim().replaceAll("\"", "");
                return finalFileName;
            }
        }
        return null;
    }

    private void writeFile(byte[] content, String filename) throws IOException {

        File file = new File(filename);

        if (!file.exists()) {
            file.createNewFile();
        }

        FileOutputStream fop = new FileOutputStream(file);

        fop.write(content);
        fop.flush();
        fop.close();

    }
}

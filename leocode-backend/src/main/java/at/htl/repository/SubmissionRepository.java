package at.htl.repository;

import at.htl.control.GitController;
import at.htl.dto.SubmissionDTO;
import at.htl.entity.Submission;
import io.quarkus.hibernate.orm.panache.Panache;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.eclipse.jgit.api.Git;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.core.MultivaluedMap;
import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@ApplicationScoped
public class SubmissionRepository implements PanacheRepository<Submission> {

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


}

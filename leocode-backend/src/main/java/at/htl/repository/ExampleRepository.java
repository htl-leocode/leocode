package at.htl.repository;

import at.htl.control.GitController;
import at.htl.entity.*;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.MultivaluedMap;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@ApplicationScoped
public class ExampleRepository implements PanacheRepository<Example> {

    @Inject
    Logger log;

    @Inject
    GitController gitController;

    @Inject
    LeocodeKeywordRepository leocodeKeywordRepository;

    public static String tmpFolderPath = "../tmpToPush";

    @Transactional
    public Example createExampleFromMultipart(MultipartFormDataInput input) {
        Map<String, List<InputPart>> inputForm = input.getFormDataMap();
        Example example = new Example();

        Repository repository = new Repository();

        List<List<InputPart>> files = new ArrayList<>();

        LeocodeFile instruction = new LeocodeFile();

        inputForm.forEach((inputType, inputParts) -> {
            try {
                switch (inputType) {
                    case "teacher":
                        log.info("teacher:" + inputParts.get(0).getBodyAsString());
                        repository.teacher = Teacher.find("name", inputParts.get(0).getBodyAsString()).firstResult();
                        instruction.author = repository.teacher.name;
                        break;
                    case "exampleName":
                        log.info("exampleName:" + inputParts.get(0).getBodyAsString());
                        example.name = inputParts.get(0).getBodyAsString();
                        break;
                    case "instruction":
                        log.info("instruction");

                        if (inputParts.get(0) != null) {
                            instruction.content = inputParts.get(0).getBody(InputStream.class, null).readAllBytes();
                            instruction.fileType = LeocodeFileType.INSTRUCTION;
                            instruction.name = inputParts.get(0).getHeaders().toString().split("filename=\"")[1].split("\"")[0];
                        }
                    case "description":
                        log.info("description:" + inputParts.get(0).getBodyAsString());
                        example.description = inputParts.get(0).getBodyAsString();
                        break;
                    case "gitRepo":
                        log.info("gitRepo:" + inputParts.get(0).getBodyAsString());
                        repository.repoUrl = inputParts.get(0).getBodyAsString();
                        break;
                    case "token":
                        log.info("token:" + inputParts.get(0).getBodyAsString());
                        repository.token = inputParts.get(0).getBodyAsString();
                        break;
                    case "exampleType":
                        log.info("exampleType:" + inputParts.get(0).getBodyAsString());
                        example.type = ExampleType.valueOf(inputParts.get(0).getBodyAsString().toUpperCase());
                        break;
                    case "blacklist":
                        example.blacklist = leocodeKeywordRepository.filterStringInput(inputParts);
                        break;
                    case "whitelist":
                        example.whitelist = leocodeKeywordRepository.filterStringInput(inputParts);
                        break;
                    default: //files
                        files.add(inputParts);
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        example.repository = repository;

        example.persist();

        instruction.example = example;
        instruction.persist();

        Git g = gitController.cloneRepositoryToDir(new File(tmpFolderPath), example.repository);

        files.forEach(inputParts -> saveFilesTemporary(inputParts, example));

        log.info("Saved all files");

        GitController.addOrInsertToGit(g, example, example.repository.token);

        g.close();

        if (!example.isValid()) {
            return null;
        }

        try {
            FileUtils.deleteDirectory(new File(tmpFolderPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return example;
    }

    void saveFilesTemporary(List<InputPart> files, Example example) {

        for (InputPart inputPart : files) {
            try (InputStream inputStream = inputPart.getBody(InputStream.class, null)) {

                MultivaluedMap<String, String> header = inputPart.getHeaders();

                String name = header.toString().split("filename=\"")[1].split("\"")[0];
                byte[] bytes = inputStream.readAllBytes();

                log.info("HEADER: " + header + " NAME: " + name);

                if (name.endsWith(".zip")) {
                    //extract zip from bytes

                    log.info(name);
                    unzipFolder(bytes, Path.of(tmpFolderPath), name.replace(".zip", ""));
                } else {

                    File file = new File(tmpFolderPath);

                    if (!file.exists()) {
                        file.mkdirs();
                    }

                    log.info("LOLOLOLO" + file.getPath());

                    try (FileOutputStream fos = new FileOutputStream(file.getPath() + "/" + name)) {
                        fos.write(bytes);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void unzipFolder(byte[] bytes, Path target, String name) throws IOException {

        try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(bytes))) {

            // list files in zip
            ZipEntry zipEntry = zis.getNextEntry();

            while (zipEntry != null) {
                boolean isDirectory = false;

                if (zipEntry.getName().endsWith(File.separator)) {
                    isDirectory = true;
                }

                String[] splitPath = zipSlipProtect(zipEntry, target).toString().split("/");

                String createdPath = "";

                for (int i = 0; i < splitPath.length; i++) {
                    if (!splitPath[i].equals(name)) {
                        createdPath = createdPath + splitPath[i];
                    }
                    if (i != splitPath.length - 1) {
                        createdPath += "/";
                    }
                }

                Path newPath = Path.of(createdPath);

                System.out.println(newPath.toString());

                if (isDirectory) {
                    Files.createDirectories(newPath);
                } else {

                    if (newPath.getParent() != null) {
                        if (Files.notExists(newPath.getParent())) {
                            Files.createDirectories(newPath.getParent());
                        }
                    }

                    Files.copy(zis, newPath, StandardCopyOption.REPLACE_EXISTING);

                }

                zipEntry = zis.getNextEntry();

            }
            zis.closeEntry();

        }

    }

    public static Path zipSlipProtect(ZipEntry zipEntry, Path targetDir)
            throws IOException {

        // test zip slip vulnerability
        // Path targetDirResolved = targetDir.resolve("../../" + zipEntry.getName());

        Path targetDirResolved = targetDir.resolve(zipEntry.getName());

        // make sure normalized file still has targetDir as its prefix
        // else throws exception
        Path normalizePath = targetDirResolved.normalize();
        if (!normalizePath.startsWith(targetDir)) {
            throw new IOException("Bad zip entry: " + zipEntry.getName());
        }

        return normalizePath;
    }


}

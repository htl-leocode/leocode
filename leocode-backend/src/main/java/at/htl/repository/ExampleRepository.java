package at.htl.repository;

import at.htl.control.GitController;
import at.htl.entity.Example;
import at.htl.entity.ExampleType;
import at.htl.entity.Repository;
import at.htl.entity.Teacher;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.eclipse.jgit.api.Git;
import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.MultivaluedMap;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ExampleRepository implements PanacheRepository<Example> {

    @Inject
    Logger log;

    @Inject
    GitController gitController;

    @Inject
    LeocodeKeywordRepository leocodeKeywordRepository;

    @Transactional
    public Example createExampleFromMultipart(MultipartFormDataInput input) {
        Map<String, List<InputPart>> inputForm = input.getFormDataMap();
        Example example = new Example();

        List<List<InputPart>> files = new ArrayList<>();

        inputForm.forEach((inputType, inputParts) -> {
            try {
                switch (inputType) {
                    case "exampleName":
                        example.name = inputParts.get(0).getBodyAsString();
                        break;
                    case "description":
                        example.description = inputParts.get(0).getBodyAsString();
                        break;
                    case "repository":
                        example.repository = Repository.find("name", inputParts.get(0).getBodyAsString()).firstResult();
                        break;
                    case "exampleType":
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

        log.info("1");
        Git g = gitController.cloneRepositoryToDir(new File("../tmpToPush/"+example.repository.name), example.repository);

        log.info("3");

        files.forEach(inputParts -> saveFilesTemporary(inputParts, example));

        log.info("6");

        GitController.addOrInsertToGit(g,example, "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");

        log.info("9");

        if (!example.isValid()) {
            return null;
        }

        example.persist();
        return example;
    }

    void saveFilesTemporary(List<InputPart> files, Example example){

        log.info("4");

        for (InputPart inputPart : files) {
            try (InputStream inputStream = inputPart.getBody(InputStream.class, null)) {

                MultivaluedMap<String, String> header = inputPart.getHeaders();

                String name = header.toString().split("filename=\"")[1].split("\"")[0];
                byte[] bytes = inputStream.readAllBytes();

                log.info("HEADER: "+ header +" NAME: " + name);

                File file = new File("../tmpToPush/"+example.repository.name+"/"+example.name);

                if(!file.exists()){
                    file.mkdirs();
                }

                try (FileOutputStream fos = new FileOutputStream(file.getPath()+"/"+name)) {
                    fos.write(bytes);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        log.info("5");

    }

}

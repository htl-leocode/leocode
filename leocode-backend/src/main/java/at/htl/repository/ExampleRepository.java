package at.htl.repository;

import at.htl.control.GitController;
import at.htl.entity.Example;
import at.htl.entity.ExampleType;
import at.htl.entity.Repository;
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

        Git g = gitController.cloneRepositoryToDir(new File("../tmpToPush/"+example.repository.name), example.repository);

        files.forEach(inputParts -> saveFilesTemporary(inputParts, example));

        GitController.addOrInsertToGit(g, example, example.repository.token);

        g.close();

        if (!example.isValid()) {
            return null;
        }

        example.persist();


        new File("../tmpToPush/"+example.repository.name).setWritable(true);

        try {
            FileUtils.deleteDirectory(new File("../tmpToPush/"+example.repository.name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return example;
    }

    void saveFilesTemporary(List<InputPart> files, Example example){

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

    }

}

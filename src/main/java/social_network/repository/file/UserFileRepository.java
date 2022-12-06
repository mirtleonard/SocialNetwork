package social_network.repository.file;

import social_network.domain.User;
import social_network.exceptions.RepositoryException;
import social_network.repository.UserRepository;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class UserFileRepository extends UserRepository {
    private String fileName;


    private void loadData() {
        Path path = Paths.get(fileName);
        try {
            List<String> lines = Files.readAllLines(path);
            lines.forEach(line -> {
                String[] values = line.split("\\|");
                if (values.length == 1) {
                    super.lastId = Integer.parseInt(values[0]);
                } else {
                    User user = new User(Integer.parseInt(values[0]), values[1]);
                    try {
                        super.add(user);
                    } catch (Exception e) {
                        System.out.println("Eroare de validare");
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            System.out.println("Eroare la citirea fisierului!\n");
            e.printStackTrace();
        }
    }

    private void saveData() {
        try {
            FileWriter writer = new FileWriter(fileName);
            String Data = "";
            Data += super.lastId + "\n";
            for(User user : super.users.values()) {
                Data = Data + user.getId() + "|" + user.getName() + "\n";
            }
            writer.write(Data);
            writer.close();
        } catch (IOException e) {
            System.out.println("Eroare la scrierea fisierului!\n");
            e.printStackTrace();
        }
    }

    public UserFileRepository(String fileName) {
        super();
        this.fileName = fileName;
        loadData();
    }

    @Override
    public void add(User user) {
        super.add(user);
        saveData();
    }

    @Override
    public void remove(Integer id) throws RepositoryException {
        super.remove(id);
        saveData();
    }
}

package social_network.repository.file;

import social_network.domain.Friendship;
import social_network.repository.FriendshipRepository;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

public class FriendshipFileRepository extends FriendshipRepository {
    String fileName;

    public FriendshipFileRepository(String fileName) {
        super();
        this.fileName = fileName;
        loadData();
    }

    public void loadData() {
        Path path = Paths.get(fileName);
        try {
            List<String> lines = Files.readAllLines(path);
            lines.forEach(line -> {
                String[] values = line.split("\\|");
                if (values.length == 1) {
                    this.lastId = Integer.parseInt(values[0]);
                } else {
                    LocalDateTime date = LocalDateTime.parse(values[3]);
                    Friendship friendship = new Friendship(Integer.parseInt(values[0]), Integer.parseInt(values[1]), Integer.parseInt(values[2]));
                    friendship.setFriendsFrom(date);
                    try {
                        super.add(friendship);
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

    public void saveData() {
        try {
            FileWriter writer = new FileWriter(fileName);
            String Data = lastId + "\n";
            for(Friendship friendship : super.friendships.values()) {
                Data = Data + friendship.getId() + "|" + friendship.getUser1() + "|" + friendship.getUser2() + "|" + friendship.getFriendsFrom() + "\n";
            }
            writer.write(Data);
            writer.close();
        } catch (IOException e) {
            System.out.println("Eroare la scrierea fisierului!\n");
            e.printStackTrace();
        }
    }

    @Override
    public void add(Friendship friendship) {
        super.add(friendship);
        saveData();
    }

    @Override
    public void remove(Integer id) {
        super.remove(id);
        saveData();
    }

}

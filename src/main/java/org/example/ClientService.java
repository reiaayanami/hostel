package org.example;

import java.util.List;

public class ClientService {
    public static List<Client> getAll() {
        return DataStore.clients;
    }

    public static void add(Client client) {
        client.setId(DataStore.clients.size() + 1);
        DataStore.clients.add(client);
    }

    public static void update(Client client) {
        // in-memory — заміна за id
        DataStore.clients.removeIf(c -> c.getId() == client.getId());
        DataStore.clients.add(client);
    }

    public static void delete(int id) {
        DataStore.clients.removeIf(c -> c.getId() == id);
    }

    public static Client getById(int id) {
        return DataStore.clients.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }
}
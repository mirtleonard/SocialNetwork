package social_network.repository;

import social_network.exceptions.RepositoryException;

import java.util.Map;

public interface Repository<ID, E> {
    void add(E entity) throws RepositoryException;
    void remove(ID id) throws RepositoryException;
    E find(ID id) throws RepositoryException;
    Map<ID, E> getAll();
}

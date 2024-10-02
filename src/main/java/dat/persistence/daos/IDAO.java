package dat.persistence.daos;

import java.util.Set;

public interface IDAO<T, I> {

    T read(I i);
    Set<T> readAll();
    T create(T t);
    T update(I i, T t);
    void delete(I i);
    boolean validatePrimaryKey(I i);

}

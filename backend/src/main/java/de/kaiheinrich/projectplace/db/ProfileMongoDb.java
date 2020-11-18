package de.kaiheinrich.projectplace.db;

import de.kaiheinrich.projectplace.model.Profile;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ProfileMongoDb extends PagingAndSortingRepository<Profile, String> {

    List<Profile> findAll();
}

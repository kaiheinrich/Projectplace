package de.kaiheinrich.projectplace.db;

import de.kaiheinrich.projectplace.model.Profile;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface ProfileMongoDb extends PagingAndSortingRepository<Profile, String> {

    List<Profile> findAll();

    Profile save(Profile profile);
}

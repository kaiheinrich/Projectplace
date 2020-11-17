package de.kaiheinrich.projectplace.db;

import de.kaiheinrich.projectplace.model.ProjectplaceUser;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserMongoDb extends PagingAndSortingRepository<ProjectplaceUser, String> {
}

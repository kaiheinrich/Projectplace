package de.kaiheinrich.projectplace.db;

import de.kaiheinrich.projectplace.model.Project;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ProjectMongoDb extends PagingAndSortingRepository<Project, String> {

    List<Project> findAll();
}

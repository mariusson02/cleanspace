package de.schonvoll.cleanspace.infrastructure.postgres.mapper;

import de.schonvoll.cleanspace.domain.entities.Workspace;
import de.schonvoll.cleanspace.domain.valueobjects.OpeningHours;
import de.schonvoll.cleanspace.domain.valueobjects.WorkspaceProperty;
import de.schonvoll.cleanspace.infrastructure.postgres.entities.WorkspaceJpaEntity;
import de.schonvoll.cleanspace.infrastructure.postgres.entities.WorkspacePropertyEmbeddable;
import org.springframework.stereotype.Component;

/**
 * Maps between Workspace domain entities and WorkspaceJpaEntity persistence objects. Handles
 * bidirectional conversion including complex nested objects like OpeningHours and
 * WorkspaceProperty.
 */
@Component
public class WorkspaceMapper {

  /**
   * Converts a JPA entity to a domain entity.
   *
   * @param jpaEntity the JPA entity to convert
   * @return the corresponding domain entity
   */
  public static Workspace toDomain(WorkspaceJpaEntity jpaEntity) {
    return Workspace.builder()
        .id(jpaEntity.getId())
        .name(jpaEntity.getName())
        .openingHours(new OpeningHours(jpaEntity.getOpeningTime(), jpaEntity.getClosingTime()))
        .capacity(jpaEntity.getCapacity())
        .properties(jpaEntity.getProperties().stream().map(WorkspaceMapper::toValueObject).toList())
        .build();
  }

  /**
   * Converts a domain entity to a JPA entity.
   *
   * @param domainEntity the domain entity to convert
   * @return the corresponding JPA entity
   */
  public static WorkspaceJpaEntity toJpaEntity(Workspace domainEntity) {
    WorkspaceJpaEntity jpaEntity = new WorkspaceJpaEntity();
    jpaEntity.setId(domainEntity.getId());
    jpaEntity.setName(domainEntity.getName());
    jpaEntity.setOpeningTime(domainEntity.getOpeningHours().open());
    jpaEntity.setClosingTime(domainEntity.getOpeningHours().close());
    jpaEntity.setCapacity(domainEntity.getCapacity());
    jpaEntity.setProperties(
        domainEntity.getProperties().stream().map(WorkspaceMapper::toEmbedded).toList());
    return jpaEntity;
  }

  private static WorkspaceProperty toValueObject(WorkspacePropertyEmbeddable embeddable) {
    return new WorkspaceProperty(embeddable.key(), embeddable.value());
  }

  private static WorkspacePropertyEmbeddable toEmbedded(WorkspaceProperty valueObject) {
    return new WorkspacePropertyEmbeddable(valueObject.key(), valueObject.value());
  }
}

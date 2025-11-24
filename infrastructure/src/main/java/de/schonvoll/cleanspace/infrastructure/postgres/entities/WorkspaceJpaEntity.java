package de.schonvoll.cleanspace.infrastructure.postgres.entities;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "workspaces")
@Getter
@Setter
@NoArgsConstructor
public class WorkspaceJpaEntity extends AuditableEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String name;
  private LocalTime openingTime;
  private LocalTime closingTime;
  private int capacity;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "workspace_properties", joinColumns = @JoinColumn(name = "workspace_id"))
  private List<WorkspacePropertyEmbeddable> properties;

  @Version private long version;
}

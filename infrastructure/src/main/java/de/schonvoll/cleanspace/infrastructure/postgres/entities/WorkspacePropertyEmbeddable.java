package de.schonvoll.cleanspace.infrastructure.postgres.entities;

import jakarta.persistence.Embeddable;

@Embeddable
public record WorkspacePropertyEmbeddable(String key, String value) {}

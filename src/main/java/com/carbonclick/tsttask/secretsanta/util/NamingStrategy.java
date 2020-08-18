package com.carbonclick.tsttask.secretsanta.util;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NamingStrategy implements PhysicalNamingStrategy {
    @Override
    public Identifier toPhysicalCatalogName(final Identifier identifier, final JdbcEnvironment jdbcEnv) {
        return identifier;
    }

    @Override
    public Identifier toPhysicalColumnName(final Identifier identifier, final JdbcEnvironment jdbcEnv) {
        return convertToSnakeCase(identifier);
    }

    @Override
    public Identifier toPhysicalSchemaName(final Identifier identifier, final JdbcEnvironment jdbcEnv) {
        return identifier;
    }

    @Override
    public Identifier toPhysicalSequenceName(final Identifier identifier, final JdbcEnvironment jdbcEnv) {
        return convertToSnakeCase(identifier);
    }

    @Override
    public Identifier toPhysicalTableName(final Identifier identifier, final JdbcEnvironment jdbcEnv) {
        Pattern pattern = Pattern.compile("^(?<unwrapped>.+)Entity$");
        Matcher matcher = pattern.matcher(identifier.getText());
        matcher.find();
        String unwrapped = matcher.group("unwrapped");
        return convertToSnakeCase(unwrapped);
    }

    private Identifier convertToSnakeCase(final Identifier identifier) {
        return convertToSnakeCase(identifier.getText());
    }

    private Identifier convertToSnakeCase(final String name) {
        final String regex = "([a-z])([A-Z])";
        final String replacement = "$1_$2";
        final String newName = name
                .replaceAll(regex, replacement)
                .toLowerCase();
        return Identifier.toIdentifier(newName);
    }
}

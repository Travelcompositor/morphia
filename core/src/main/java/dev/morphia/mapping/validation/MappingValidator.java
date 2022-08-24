package dev.morphia.mapping.validation;

import dev.morphia.annotations.Property;
import dev.morphia.annotations.Reference;
import dev.morphia.mapping.Mapper;
import dev.morphia.mapping.codec.pojo.EntityModel;
import dev.morphia.mapping.validation.ConstraintViolation.Level;
import dev.morphia.mapping.validation.classrules.DuplicatedAttributeNames;
import dev.morphia.mapping.validation.classrules.EntityAndEmbed;
import dev.morphia.mapping.validation.classrules.EntityOrEmbed;
import dev.morphia.mapping.validation.classrules.MultipleId;
import dev.morphia.mapping.validation.classrules.MultipleVersions;
import dev.morphia.mapping.validation.classrules.ShardKeyNames;
import dev.morphia.mapping.validation.fieldrules.ContradictingAnnotations;
import dev.morphia.mapping.validation.fieldrules.IdDoesNotMix;
import dev.morphia.mapping.validation.fieldrules.LazyReferenceMissingDependencies;
import dev.morphia.mapping.validation.fieldrules.LazyReferenceOnArray;
import dev.morphia.mapping.validation.fieldrules.MapKeyTypeConstraint;
import dev.morphia.mapping.validation.fieldrules.ReferenceToUnidentifiable;
import dev.morphia.mapping.validation.fieldrules.VersionMisuse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static java.lang.String.format;
import static java.util.Collections.sort;

/**
 * Validator for mapped types
 */
public class MappingValidator {

    private static final Logger LOG = LoggerFactory.getLogger(MappingValidator.class);

    /**
     * @param entityModel the EntityModel to validate
     * @param mapper      the Mapper to use for validation
     * @morphia.internal
     */
    public void validate(Mapper mapper, EntityModel entityModel) {
        final Set<ConstraintViolation> ve = new TreeSet<>((o1, o2) -> o1.getLevel().ordinal() > o2.getLevel().ordinal() ? -1 : 1);

        final List<ClassConstraint> rules = getConstraints();
        for (ClassConstraint v : rules) {
            v.check(mapper, entityModel, ve);
        }

        if (!ve.isEmpty()) {
            final ConstraintViolation worst = ve.iterator().next();
            final Level maxLevel = worst.getLevel();
            if (maxLevel.ordinal() >= Level.FATAL.ordinal()) {
                throw new ConstraintViolationException(ve);
            }

            // sort by class to make it more readable
            final List<LogLine> l = new ArrayList<>();
            for (ConstraintViolation v : ve) {
                l.add(new LogLine(v));
            }
            sort(l);

            for (LogLine line : l) {
                line.log();
            }
        }
    }

    private List<ClassConstraint> getConstraints() {
        final List<ClassConstraint> constraints = new ArrayList<>(32);

        // class-level
        constraints.add(new MultipleId());
        constraints.add(new MultipleVersions());
        constraints.add(new EntityAndEmbed());
        constraints.add(new EntityOrEmbed());
        constraints.add(new DuplicatedAttributeNames());
        // field-level
        constraints.add(new IdDoesNotMix());
        constraints.add(new ReferenceToUnidentifiable());
        constraints.add(new LazyReferenceMissingDependencies());
        constraints.add(new LazyReferenceOnArray());
        constraints.add(new MapKeyTypeConstraint());
        constraints.add(new VersionMisuse());
        constraints.add(new ShardKeyNames());

        constraints.add(new ContradictingAnnotations(Reference.class, Property.class));

        return constraints;
    }

    static class LogLine implements Comparable<LogLine> {
        private final ConstraintViolation v;

        LogLine(ConstraintViolation v) {
            this.v = v;
        }

        @Override
        public int compareTo(LogLine o) {
            return v.getPrefix().compareTo(o.v.getPrefix());
        }

        @Override
        public int hashCode() {
            return v.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            final LogLine logLine = (LogLine) o;

            return v.equals(logLine.v);

        }

        void log() {
            switch (v.getLevel()) {
                case SEVERE:
                    LOG.error(v.render());
                    break;
                case WARNING:
                    LOG.warn(v.render());
                    break;
                case INFO:
                    LOG.info(v.render());
                    break;
                case MINOR:
                    LOG.debug(v.render());
                    break;
                default:
                    throw new IllegalStateException(format("Cannot log %s of Level %s", ConstraintViolation.class.getSimpleName(),
                                                           v.getLevel()));
            }
        }
    }
}

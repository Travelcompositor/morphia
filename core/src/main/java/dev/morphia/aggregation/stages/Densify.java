package dev.morphia.aggregation.stages;

import dev.morphia.aggregation.expressions.TimeUnit;
import dev.morphia.annotations.internal.MorphiaInternal;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * Creates new documents in a sequence of documents where certain values in a field are missing.
 * <p>
 * You can use $densify to:
 * <ul>
 * <li>Fill gaps in time series data.
 * <li>Add missing values between groups of data.
 * <li>Populate your data with a specified range of values.
 * </ul>
 *
 * @aggregation.expression $densify
 * @mongodb.server.release 5.1
 * @since 2.3
 */
public class Densify extends Stage {
    private final String field;
    private final Range range;
    private List<String> partitionByFields;

    private Densify(String field, Range range) {
        super("$densify");
        this.field = field;
        this.range = range;
    }

    /**
     * Creates new documents in a sequence of documents where certain values in a field are missing.
     * <p>
     * You can use $densify to:
     * <ul>
     * <li>Fill gaps in time series data.
     * <li>Add missing values between groups of data.
     * <li>Populate your data with a specified range of values.
     * </ul>
     *
     * @param field The field to densify. The values of the specified field must either be all numeric values or all dates.
     * @param range specifies how the data is densified.
     * @return the new stage
     * @aggregation.expression $densify
     * @mongodb.server.release 5.1
     * @since 2.3
     */
    public static Densify densify(String field, Range range) {
        return new Densify(field, range);
    }

    /**
     * @return this
     * @morphia.internal
     */
    @MorphiaInternal
    public String field() {
        return field;
    }

    /**
     * The set of fields to act as the compound key to group the documents. In the $densify stage, each group of documents is known as a
     * partition.
     * <p>
     * If you omit this field, $densify uses one partition for the entire collection.
     *
     * @param partitionByFields the fields to partition by
     * @return this
     */
    public Densify partitionByFields(String... partitionByFields) {
        this.partitionByFields = asList(partitionByFields);
        return this;
    }

    /**
     * @return this
     * @morphia.internal
     */
    @MorphiaInternal
    public List<String> partitionByFields() {
        return partitionByFields;
    }

    /**
     * @return this
     * @morphia.internal
     */
    @MorphiaInternal
    public Range range() {
        return range;
    }

    /**
     * @morphia.internal
     */
    @MorphiaInternal
    public enum RangeType {
        BOUNDED,
        FULL,
        PARTITION
    }

    public static class Range {
        private final RangeType type;
        private final Number step;
        private TimeUnit unit;
        private Object lowerBound;
        private Object upperBound;

        protected Range(RangeType type, Number step) {
            this.type = type;
            this.step = step;
        }

        /**
         * Creates a bounded range.
         *
         * @param lowerBound the lower bound
         * @param upperBound the upper bound
         * @param step
         * @return the Range to pass to {@link #densify(String, Range)}
         */
        public static Range bounded(Object lowerBound, Object upperBound, Number step) {
            return new Range(RangeType.BOUNDED, step)
                    .bounds(lowerBound, upperBound);
        }

        private Range bounds(Object lowerBound, Object upperBound) {
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
            return this;
        }

        /**
         * Creates a "full" range with documents spanning the full range of values of the field being densified.
         *
         * @param step the step size to use
         * @return the Range to pass to {@link #densify(String, Range)}
         */
        public static Range full(Number step) {
            return new Range(RangeType.FULL, step);
        }

        /**
         * Creates a partitioned range and adds documents to each partition, similar to if you had run a full range densification on each
         * partition individually.
         *
         * @param step the step size
         * @return the Range to pass to {@link #densify(String, Range)}
         */
        public static Range partition(Number step) {
            return new Range(RangeType.PARTITION, step);
        }

        /**
         * @return this
         * @morphia.internal
         */
        @MorphiaInternal
        public Object lowerBound() {
            return lowerBound;
        }

        /**
         * @return this
         * @morphia.internal
         */
        @MorphiaInternal
        public Number step() {
            return step;
        }

        /**
         * @return this
         * @morphia.internal
         */
        @MorphiaInternal
        public RangeType type() {
            return type;
        }

        /**
         * @return this
         * @morphia.internal
         */
        @MorphiaInternal
        public TimeUnit unit() {
            return unit;
        }

        /**
         * The unit to apply to the step field when incrementing date values in field.
         *
         * @param unit the unit to use
         * @return the Range itself
         */
        public Range unit(TimeUnit unit) {
            this.unit = unit;
            return this;
        }

        /**
         * @return this
         * @morphia.internal
         */
        @MorphiaInternal
        public Object upperBound() {
            return upperBound;
        }
    }
}

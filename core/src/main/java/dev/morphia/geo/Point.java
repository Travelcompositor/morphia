package dev.morphia.geo;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.client.model.geojson.Position;
import com.mongodb.lang.Nullable;

import dev.morphia.annotations.internal.MorphiaInternal;

/**
 * Represents a GeoJSON Point type. Will be persisted into the database according to <a href="http://geojson.org/geojson-spec.html#id2">the
 * specification</a>. Therefore because of this, this entity will never have its own ID or store the its Class name.
 * <p/>
 * The builder for creating a Point is the {@code GeoJson.pointBuilder} method, or the helper {@code GeoJson.point} factory method.
 *
 * @deprecated use the driver-provided types instead
 */
@SuppressWarnings("removal")
@Deprecated(since = "2.0", forRemoval = true)
public class Point implements Geometry {
    private final List<Double> coordinates = new ArrayList<Double>();

    Point(double latitude, double longitude) {
        coordinates.add(longitude);
        coordinates.add(latitude);
    }

    Point(List<Double> coordinates) {
        this.coordinates.addAll(coordinates);
    }

    @Override
    public List<Double> getCoordinates() {
        return coordinates;
    }

    /**
     * Return the latitude of this point.
     *
     * @return the Point's latitude
     */
    public double getLatitude() {
        return coordinates.get(1);
    }

    /**
     * Return the longitude of this point.
     *
     * @return the Point's longitude
     */
    public double getLongitude() {
        return coordinates.get(0);
    }

    @Override
    public int hashCode() {
        return coordinates.hashCode();
    }

    /* equals, hashCode and toString. Useful primarily for testing and debugging. Don't forget to re-create when changing this class */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Point point = (Point) o;

        if (getCoordinates().size() != point.getCoordinates().size()) {
            return false;
        }
        for (int i = 0; i < coordinates.size(); i++) {
            final Double coordinate = coordinates.get(i);
            if (Double.compare(coordinate, point.getCoordinates().get(i)) != 0) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return String.format("Point{coordinates=%s}", coordinates);
    }

    /**
     * @morphia.internal
     * @return this Point converted to a driver Point
     */
    @Override
    @MorphiaInternal
    public com.mongodb.client.model.geojson.Point convert() {
        return convert(null);
    }

    /**
     * @return this Point converted to a driver Point
     * @morphia.internal
     */
    @Override
    @MorphiaInternal
    public com.mongodb.client.model.geojson.Point convert(@Nullable CoordinateReferenceSystem crs) {
        return new com.mongodb.client.model.geojson.Point(crs != null ? crs.convert() : null, new Position(getLongitude(), getLatitude()));
    }
}

package dev.morphia.geo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mongodb.lang.Nullable;

import dev.morphia.annotations.Id;

import org.bson.types.ObjectId;

/**
 * Represents a GeoJSON LineString type. Will be persisted into the database according to <a href="http://geojson.org/geojson-spec
 * .html#id3">the
 * specification</a>.
 * <p/>
 * The factory for creating a LineString is the {@code GeoJson.lineString} method.
 *
 * @see dev.morphia.geo.GeoJson#lineString(Point...)
 * @deprecated use the driver-provided types instead
 */
@SuppressWarnings("removal")
@Deprecated(since = "2.0", forRemoval = true)
public class LineString implements Geometry {
    @Id
    private ObjectId id;
    private final List<Point> coordinates;

    @SuppressWarnings("UnusedDeclaration") // used by Morphia
    private LineString() {
        coordinates = new ArrayList<>();
    }

    LineString(Point... points) {
        this.coordinates = Arrays.asList(points);
    }

    LineString(List<Point> points) {
        coordinates = points;
    }

    @Override
    public List<Point> getCoordinates() {
        return coordinates;
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

        LineString that = (LineString) o;

        return coordinates.equals(that.coordinates);
    }

    @Override
    public String toString() {
        return "LineString{"
                + "coordinates=" + coordinates
                + '}';
    }

    @Override
    public com.mongodb.client.model.geojson.LineString convert() {
        return convert(null);
    }

    @Override
    public com.mongodb.client.model.geojson.LineString convert(@Nullable CoordinateReferenceSystem crs) {
        return new com.mongodb.client.model.geojson.LineString(crs != null ? crs.convert() : null, GeoJson.convertPoints(coordinates));
    }
}

package smarthomesimulator.dto;

import smarthomesimulator.ValidationUtils;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class RoomUpdateDto {
    private final String name;
    private final List<String> deviceIds;

    public RoomUpdateDto(String name, List<String> deviceIds) {
        // Name is optional but if provided must not be blank
        if (name != null && name.trim().isEmpty()) {
            throw new IllegalArgumentException("Room name cannot be empty");
        }
        this.name = name != null ? name.trim() : null;

        // Device IDs list is optional but if provided must not be null
        if (deviceIds != null) {
            this.deviceIds = List.copyOf(deviceIds);
        } else {
            this.deviceIds = null;
        }
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public Optional<List<String>> getDeviceIds() {
        return Optional.ofNullable(deviceIds);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomUpdateDto that = (RoomUpdateDto) o;
        return Objects.equals(name, that.name) &&
               Objects.equals(deviceIds, that.deviceIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, deviceIds);
    }
}

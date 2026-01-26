package smarthomesimulator.dto;

import smarthomesimulator.ValidationUtils;
import java.util.List;
import java.util.Objects;

public final class RoomStoreDto {
    private final String name;
    private final List<String> deviceIds;

    public RoomStoreDto(String name, List<String> deviceIds) {
        ValidationUtils.requireNotBlank(name, "Room name");
        ValidationUtils.requireNotNull(deviceIds, "Device IDs list");

        this.name = name.trim();
        this.deviceIds = List.copyOf(deviceIds);
    }

    public String getName() {
        return name;
    }

    public List<String> getDeviceIds() {
        return deviceIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomStoreDto that = (RoomStoreDto) o;
        return Objects.equals(name, that.name) &&
               Objects.equals(deviceIds, that.deviceIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, deviceIds);
    }
}

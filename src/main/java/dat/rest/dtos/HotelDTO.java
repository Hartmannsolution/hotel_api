package dat.rest.dtos;

import dat.persistence.entities.Hotel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class HotelDTO {

    private Integer id;
    private String hotelName;
    private String hotelAddress;
    private Hotel.HotelType hotelType;
    private Set<RoomDTO> rooms = new HashSet<>();

    public HotelDTO(Hotel hotel) {
        this.id = hotel.getId();
        this.hotelName = hotel.getHotelName();
        this.hotelAddress = hotel.getHotelAddress();
        this.hotelType = hotel.getHotelType();
        if (hotel.getRooms() != null)
        {
            hotel.getRooms().forEach( room -> rooms.add(new RoomDTO(room)));
        }
    }

    public HotelDTO(String hotelName, String hotelAddress, Hotel.HotelType hotelType)
    {
        this.hotelName = hotelName;
        this.hotelAddress = hotelAddress;
        this.hotelType = hotelType;
    }

    public static List<HotelDTO> toHotelDTOList(List<Hotel> hotels) {
        return hotels.stream().map(HotelDTO::new).collect(Collectors.toList());
    }

    // equals NOT on ID, but on name, address and type
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HotelDTO hotelDto)) return false;
        if(!getHotelName().equals(hotelDto.getHotelName())) return false;
        if(!getHotelAddress().equals(hotelDto.getHotelAddress())) return false;
        return getHotelType() == hotelDto.getHotelType();
    }

    @Override
    public int hashCode()
    {
        return getId().hashCode();
    }

}

package abayomi.code.parking;

import java.io.Serializable;

/**
 * Created by Abayomi S on 21.09.2016.
 */
public class Parking implements Serializable {
    //String coordinates;
    String lat;
    String lng;
    int id;
    int capacity;
    String status;
    String parkingName;

    public Parking() {
    }
}

/*******************************Copyright Block*********************************
 *                                                                             *
 *                Sally Prayer Times Calculator (Final 24.1.20)                *
 *           Copyright (C) 2015 http://www.sallyprayertimes.com/               *
 *                         bibali1980@gmail.com                                *
 *                                                                             *
 *     This program is free software: you can redistribute it and/or modify    *
 *     it under the terms of the GNU General Public License as published by    *
 *      the Free Software Foundation, either version 3 of the License, or      *
 *                      (at your option) any later version.                    *
 *                                                                             *
 *       This program is distributed in the hope that it will be useful,       *
 *        but WITHOUT ANY WARRANTY; without even the implied warranty of       *
 *        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the        *
 *                 GNU General Public License for more details.                *
 *                                                                             *
 *      You should have received a copy of the GNU General Public License      *
 *      along with this program.  If not, see http://www.gnu.org/licenses      *
 *******************************************************************************/
package classes;

public class KiblaDirectionCalculator {
	public static final double QIBLA_LATITUDE = Math.toRadians(21.423333);
    public static final double QIBLA_LONGITUDE = Math.toRadians(39.823333);

    public static float getQiblaDirectionFromNorth(double degLongitude,double degLatitude) {
    double latitude2 = Math.toRadians(degLatitude);
    double longitude = Math.toRadians(degLongitude);

    double soorat = Math.sin(QIBLA_LONGITUDE - longitude);
    double makhraj = Math.cos(latitude2) * Math.tan(QIBLA_LATITUDE)- Math.sin(latitude2) * Math.cos(QIBLA_LONGITUDE - longitude);
    double returnValue = Math.toDegrees(Math.atan(soorat / makhraj));

        if (latitude2 > QIBLA_LATITUDE) {
            if ((longitude > QIBLA_LONGITUDE || longitude < (Math
                    .toRadians(-180d) + QIBLA_LONGITUDE))
                    && (returnValue > 0 && returnValue <= 90)) {

                returnValue += 180;

            } else if (!(longitude > QIBLA_LONGITUDE || longitude < (Math
                    .toRadians(-180d) + QIBLA_LONGITUDE))
                    && (returnValue > -90 && returnValue < 0)) {

                returnValue += 180;

            }

        }
        if (latitude2 < QIBLA_LATITUDE) {

            if ((longitude > QIBLA_LONGITUDE || longitude < (Math
                    .toRadians(-180d) + QIBLA_LONGITUDE))
                    && (returnValue > 0 && returnValue < 90)) {

                returnValue += 180;

            }
            if (!(longitude > QIBLA_LONGITUDE || longitude < (Math
                    .toRadians(-180d) + QIBLA_LONGITUDE))
                    && (returnValue > -90 && returnValue <= 0)) {

                returnValue += 180;
            }

        }
        return (float)(returnValue);
    }

}

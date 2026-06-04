package cn.iocoder.yudao.module.tutor.util;

import java.math.BigDecimal;

/**
 * 家教 LBS geohash 工具。
 */
public final class TutorGeoHashUtils {

    private static final char[] BASE32 = "0123456789bcdefghjkmnpqrstuvwxyz".toCharArray();

    private TutorGeoHashUtils() {
    }

    public static String encode(BigDecimal longitude, BigDecimal latitude) {
        return encode(longitude, latitude, 7);
    }

    public static String encode(BigDecimal longitude, BigDecimal latitude, int precision) {
        if (longitude == null || latitude == null) {
            return null;
        }
        double[] lngRange = {-180.0, 180.0};
        double[] latRange = {-90.0, 90.0};
        StringBuilder hash = new StringBuilder(precision);
        boolean even = true;
        int bit = 0;
        int ch = 0;
        while (hash.length() < precision) {
            double value = even ? longitude.doubleValue() : latitude.doubleValue();
            double[] range = even ? lngRange : latRange;
            double mid = (range[0] + range[1]) / 2;
            if (value >= mid) {
                ch |= 1 << (4 - bit);
                range[0] = mid;
            } else {
                range[1] = mid;
            }
            even = !even;
            if (bit < 4) {
                bit++;
            } else {
                hash.append(BASE32[ch]);
                bit = 0;
                ch = 0;
            }
        }
        return hash.toString();
    }
}

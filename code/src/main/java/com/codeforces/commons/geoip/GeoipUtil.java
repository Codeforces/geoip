package com.codeforces.commons.geoip;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Mike Mirzayanov (mirzayanovmr@gmail.com)
 */
public class GeoipUtil {
    private static final LookupService LOOKUP_COUNTRY_SERVICE;

    private GeoipUtil() {
        throw new UnsupportedOperationException();
    }

    /**
     * @param ip IPv4 or IPv6 ip-address.
     * @return The ISO two-letter country code of country or "--". Example: RU.
     */
    @Nonnull
    public static String getCountryCodeByIp(@Nonnull String ip) {
        if (ip.contains(":")) {
            return LOOKUP_COUNTRY_SERVICE.getCountryV6(ip).getCode();
        } else {
            return LOOKUP_COUNTRY_SERVICE.getCountry(ip).getCode();
        }
    }

    static {
        String countryResourcePath = "/com/codeforces/commons/geoip/GeoIP.dat";

        try {
            LOOKUP_COUNTRY_SERVICE = new LookupService(
                    FileUtils.toFile(GeoipUtil.class.getResource(countryResourcePath).toURI().toURL()),
                    LookupService.GEOIP_MEMORY_CACHE
            );
        } catch (IOException e) {
            throw new RuntimeException("Can't read GeoIP.dat from resource '" + countryResourcePath + "'.", e);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Can't parse URL of resource '" + countryResourcePath + "'.", e);
        }
    }
}

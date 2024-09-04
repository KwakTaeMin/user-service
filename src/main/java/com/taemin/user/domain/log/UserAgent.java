package com.taemin.user.domain.log;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ua_parser.Client;
import ua_parser.Parser;

@Getter
@Embeddable
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class UserAgent {

    private String browserFamily;
    private String browserVersion;
    private String osFamily;
    private String osVersion;
    private String deviceFamily;

    private UserAgent(String browserFamily, String browserVersion, String osFamily, String osVersion, String deviceFamily) {
        this.browserFamily = browserFamily;
        this.browserVersion = browserVersion;
        this.osFamily = osFamily;
        this.osVersion = osVersion;
        this.deviceFamily = deviceFamily;
    }

    public static UserAgent of(String userAgentString) {
        Parser userAgentParser = new Parser();
        Client client = userAgentParser.parse(userAgentString);
        String browserFamily = client.userAgent.family;
        String browserVersion = client.userAgent.major + "." + client.userAgent.minor + "." + client.userAgent.patch;
        String osFamily = client.os.family;
        String osVersion = client.os.major + "." + client.os.minor + "." + client.os.patch;
        String deviceFamily = client.device.family;
        return new UserAgent(browserFamily, browserVersion, osFamily, osVersion, deviceFamily);
    }
}
